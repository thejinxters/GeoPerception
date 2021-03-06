package com.geoperception.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;
import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by johnluke on 4/9/15.
 */
public class TopHashtagsBolt extends BaseRichBolt {

    OutputCollector outputCollector;
    Map<String,String> config;
    Cluster cassandraCluster;
    Session session;
    String cql;
    PreparedStatement statement;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        outputCollector = collector;
        config = (Map <String,String>) stormConf;
        cassandraCluster = setupCasandraClient(config.get("cassandra.nodes").split(","));
        session = getSessionWithRetry(cassandraCluster, config.get("cassandra.keyspace"));
    }

    @Override
    public void execute(Tuple input) {
        String hashtag = (String) input.getValueByField("hashtag");

        //Collect Count of hashtag from database
        Statement getHashtagCount = QueryBuilder.select()
                .from("hashtag_counts")
                .where(QueryBuilder.eq("hashtag", hashtag));
        Row row = session.execute(getHashtagCount).one();
        Long count = row.getLong("count");


        // Collect all top hashtags
        Statement getTopHashtags = QueryBuilder.select()
                .all().from("top_hashtags");
        List<Row> topHashtags = session.execute(getTopHashtags).all();


        Long lowestCount = null;
        String lowestHashtag = null;
        boolean inTopHashtags = false;
        boolean topHashtagsNotFull = false;

        if ( topHashtags.size() < 200) {
            // Hahstag table not filled yet
            topHashtagsNotFull = true;
        }

        // logic for determining if current is top hashtag
        for (Row hashtagRow : topHashtags){
            String currentName = hashtagRow.getString("hashtag");
            Long currentCount = hashtagRow.getLong("count");
            if (lowestCount == null){
                lowestHashtag = currentName;
                lowestCount = currentCount;
            }
            if (currentName.equals(hashtag)){
                // Already in top hashtags
                inTopHashtags = true;
            }
            if (currentCount < lowestCount){
                lowestHashtag = currentName;
                lowestCount = currentCount;
            }
        }

        Statement addTopHashtag = null;

        if (topHashtagsNotFull || inTopHashtags){
            // ADD IT!!
            addTopHashtag = QueryBuilder.update("top_hashtags")
                    .with(QueryBuilder.set("count", count))
                    .where(QueryBuilder.eq("hashtag", hashtag));
        }
        else if (count > lowestCount){
            // REMOVE LOWEST HASHTAG AND ADD THIS ONE
            Statement removeTopHashtag = QueryBuilder.delete()
                    .from("top_hashtags")
                    .where(QueryBuilder.eq("hashtag", lowestHashtag));
            session.execute(removeTopHashtag);
            addTopHashtag = QueryBuilder.insertInto("top_hashtags")
                    .value("hashtag", hashtag)
                    .value("count", count);
        }

        if (addTopHashtag != null){
            session.execute(addTopHashtag);
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public static Cluster setupCasandraClient(String [] nodes){
        //Setup Cassandra Policies
        return Cluster.builder()
                .withoutJMXReporting()
                .withoutMetrics()
                .addContactPoints(nodes)
                .withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
                .withReconnectionPolicy(new ExponentialReconnectionPolicy(100L, TimeUnit.MINUTES.toMillis(5)))
                .withLoadBalancingPolicy(new TokenAwarePolicy(new RoundRobinPolicy()))
                .build();
    }

    public static Session getSessionWithRetry(Cluster cluster, String keyspace) {
        while (true) {
            try {
                // Attempt connection to cluster keyspace
                return cluster.connect(keyspace);
            }
            catch (NoHostAvailableException e){
                System.err.println("All Cassandra Hosts offline. Waiting to try again");
                Utils.sleep(1000);
            }
        }
    }
}
