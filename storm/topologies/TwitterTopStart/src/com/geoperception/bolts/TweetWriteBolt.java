package com.geoperception.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jinxters on 4/17/15.
 */
public class TweetWriteBolt extends BaseRichBolt {
    OutputCollector outputCollector;
    Map<String,String> config;
    Cluster cassandraCluster;
    Session session;
    String component;
    String cql;
    PreparedStatement statement;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        outputCollector = collector;
        config = (Map <String,String>) stormConf;
        cassandraCluster = setupCasandraClient(config.get("cassandra.nodes").split(","));
        session = getSessionWithRetry(cassandraCluster, config.get("cassandra.keyspace"));
        component = context.getThisComponentId();
        cql = config.get(component + ".cql");

        System.out.println("Statement = " + cql);
        Preconditions.checkArgument(cql != null, "CassandraWriteBolt: " + component +
                " is missing a cql statement in bolt config");

        statement = session.prepare(cql);

    }

    @Override
    public void execute(Tuple input) {

        Object id = input.getValueByField("id");
        Object content = input.getValueByField("content");
        Object userName = input.getValueByField("userName");
        Object createdAt = input.getValueByField("createdAt");
        Object lat = input.getValueByField("lat");
        Object lng = input.getValueByField("lng");
        Object city = input.getValueByField("city");
        Object hashtags = input.getValueByField("hashTagText");
        Object[] tweetArray = new Object[] {content, userName, createdAt, lat, lng, city, hashtags, id};

        try{
            BoundStatement bound = statement.bind(tweetArray);
            session.execute(bound);
            outputCollector.ack(input);
            System.out.println("Writing tweet to cassandra - User: " + userName);
        }
        catch (Throwable t) {
            outputCollector.reportError(t);
            outputCollector.fail(input);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // Not used
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
