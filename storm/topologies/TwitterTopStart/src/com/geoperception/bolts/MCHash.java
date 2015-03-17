package com.geoperception.bolts;

import java.util.Map;

import twitter4j.HashtagEntity;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class MCHash extends BaseBasicBolt{
	
	private Map<String,Map<String,Integer>> hashTagCounter;
	private Map<String,String> countryTags;

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String country = (String) tuple.getValueByField("country");
		HashtagEntity[] hashtags = (HashtagEntity[])tuple.getValueByField("hashtags");
		for (int i = 0; i < hashtags.length; i ++){		
			String tag = hashtags[i].getText();
			hashTagCounter.get(country);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
		
	}

}
