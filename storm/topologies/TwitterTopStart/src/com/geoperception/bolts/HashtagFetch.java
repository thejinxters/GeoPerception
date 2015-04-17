package com.geoperception.bolts;

import twitter4j.HashtagEntity;
import twitter4j.Place;
import twitter4j.Status;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class HashtagFetch extends BaseBasicBolt{

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		Status status = (Status) tuple.getValueByField("tweet");
		HashtagEntity[] hashtags = status.getHashtagEntities();
		Place place = status.getPlace();
        if (place != null && hashtags != null) {
            String country = place.getCountry();
            collector.emit(new Values(country, hashtags));
        }
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("country","hashtags"));
		
	}

}
