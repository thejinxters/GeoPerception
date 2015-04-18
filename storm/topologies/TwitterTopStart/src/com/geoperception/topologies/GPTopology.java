package com.geoperception.topologies;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import com.geoperception.bolts.LocationFetch;

import com.geoperception.spouts.BasicTwitterSpout;

public class GPTopology {

	public static void main(String[] args){

		String consumerKey = args[0];
		String consumerSecret = args[1];
		String accessToken = args[2];
		String accessTokenSecret = args[3];
        String GoogleAuth = args[4];

		String[] arguments = args.clone();
    String[] keyWords = Arrays.copyOfRange(arguments, 4, arguments.length);

    //***************************Properties***************************
    Properties properties = new Properties();
    try {
        InputStream is = StartingTopology.class.getClassLoader()
                .getResourceAsStream("development.properties");
        if (is == null) {
            System.out.println(StartingTopology.class.getClassLoader());
            throw new RuntimeException("Classpath missing development.properties file");
        }
        properties.load(is);
    }
    catch (IOException e) {
        throw new RuntimeException(e);
        }


		TopologyBuilder builder = new TopologyBuilder();

		//***************************Spouts***************************
		builder.setSpout("tweetspout", new BasicTwitterSpout(consumerKey,consumerSecret,
				accessToken,accessTokenSecret,keyWords),5);

		//***************************Bolts***************************

		builder.setBolt("loc", new LocationFetch()).shuffleGrouping("tweetspout");
//		builder.setBolt("count", new LocationCounter()).fieldsGrouping("loc", new Fields("place"));
//        builder.setBolt("hash",new HashtagFetch()).shuffleGrouping("tweetspout");
//        builder.setBolt("mchash",new MCHash()).fieldsGrouping("hash", new Fields("country"));
//		builder.setBolt("print", new PrinterBolt()).shuffleGrouping("count");
//        builder.setBolt("geocode", new GeoCoderator(GoogleAuth)).shuffleGrouping("loc");
//        builder.setBolt("output", new TextFiler()).shuffleGrouping("geocode");

		//***************************Start Stream***************************
		Config conf = new Config();

        for (String name : properties.stringPropertyNames()) {
            conf.put(name, properties.getProperty(name));
        }


        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology("starter", conf, builder.createTopology());

        Utils.sleep(600000);
        cluster.shutdown();
	}

}
