package com.geoperception.startingtop;

import java.util.Arrays;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class StartingTopology {
	
	public static void main(String[] args){
		
		String consumerKey = args[0];
		String consumerSecret = args[1];
		String accessToken = args[2];
		String accessTokenSecret = args[3];
		
		String[] arguments = args.clone();
        String[] keyWords = Arrays.copyOfRange(arguments, 4, arguments.length);
		
		
		TopologyBuilder builder = new TopologyBuilder();
		
		//***************************Spouts***************************
		builder.setSpout("tweetspout", new BasicTwitterSpout(consumerKey,consumerSecret,
				accessToken,accessTokenSecret,keyWords),5);
		
		//***************************Bolts***************************
		
		builder.setBolt("loc", new LocationFetch()).shuffleGrouping("tweetspout");
		builder.setBolt("count", new LocationCounter()).shuffleGrouping("loc");
		builder.setBolt("print", new PrinterBolt()).shuffleGrouping("count");
		
		//***************************Start Stream***************************
		Config conf = new Config();
		

        LocalCluster cluster = new LocalCluster();
        
        cluster.submitTopology("starter", conf, builder.createTopology());
        
        Utils.sleep(600000);
        cluster.shutdown();
	}

}
