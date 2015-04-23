package com.geoperception.bolts;



import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jinxters on 4/17/15.
 */
public class HashtagUpdateBolt extends BaseRichBolt {

    OutputCollector outputCollector;
    Map<String,String> config;
    Cluster cassandraCluster;
    Session session;
    String cql;
    PreparedStatement statement;
    String counts_table = "hashtag_counts";
    String tweetid_table = "hashtag_tweets";

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
        Long tweetId = (Long) input.getValueByField("tweetId");

        try{
            Row row = updateHashtag(hashtag, tweetId);
            Long count;
            count = row.getLong("count");
            System.out.println("Writing hashtag to cassandra -  "+hashtag+": "+count);
        }
        catch (Throwable t) {
            outputCollector.reportError(t);
            outputCollector.fail(input);
        }

        outputCollector.emit(new Values(hashtag));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("hashtag"));
    }

    public Row updateHashtag(String hashtag, Long tweetId) {
        Statement incrementCounter = QueryBuilder.update(counts_table)
                .with(QueryBuilder.incr("count", 1))
                .where(QueryBuilder.eq("hashtag", hashtag));
        session.execute(incrementCounter).one();

        Statement addTweetId = QueryBuilder.update(tweetid_table)
                .with(QueryBuilder.append("tweetIds", tweetId ))
                .where(QueryBuilder.eq("hashtag", hashtag));
        session.execute(addTweetId);
        Statement selectRow = QueryBuilder.select()
                .from(counts_table)
                .where(QueryBuilder.eq("hashtag", hashtag));
        return session.execute(selectRow).one();
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
