package com.geoperception.topologies;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import com.geoperception.bolts.*;
import com.geoperception.spouts.BasicTwitterSpout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by johnluke on 4/9/15.
 */
public class TweetStorageTopology {

    public static void main(String[] args){

        String consumerKey = args[0];
        String consumerSecret = args[1];
        String accessToken = args[2];
        String accessTokenSecret = args[3];

        String[] arguments = args.clone();
        String[] keyWords = Arrays.copyOfRange(arguments, 4, arguments.length);

        //***************************Properties***************************
        Properties properties = new Properties();
        try {
            InputStream is = TweetStorageTopology.class.getClassLoader()
                    .getResourceAsStream("development.properties");
            if (is == null) {
                System.out.println(TweetStorageTopology.class.getClassLoader());
                throw new RuntimeException("Classpath missing development.properties file");
            }
            properties.load(is);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


        TopologyBuilder builder = new TopologyBuilder();

        //***************************Spouts***************************
        builder.setSpout("tweetspout", new BasicTwitterSpout(consumerKey, consumerSecret,
                accessToken, accessTokenSecret, keyWords), 5);

        //***************************Bolts***************************

        builder.setBolt("limiter", new GeotagLimiterBolt()).shuffleGrouping("tweetspout");
        builder.setBolt("parser", new ParseTweetDataBolt()).shuffleGrouping("limiter");
        builder.setBolt("saveTweet", new TweetWriteBolt()).shuffleGrouping("parser");
        builder.setBolt("hashtagEmitter", new HashtagEmitterBolt()).shuffleGrouping("parser");
        builder.setBolt("writeHashtag", new HashtagUpdateBolt()).fieldsGrouping("hashtagEmitter", new Fields("hashtag"));
        builder.setBolt("tophashtag", new TopHashtagsBolt()).fieldsGrouping("writeHashtag", new Fields("hashtag"));

        
        //***************************Start Stream***************************
        Config conf = new Config();

        for (String name : properties.stringPropertyNames()) {
            conf.put(name, properties.getProperty(name));
        }


        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology("starter", conf, builder.createTopology());

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
//
        cluster.shutdown();
//        Utils.sleep(600000);
//        cluster.shutdown();
    }
}
