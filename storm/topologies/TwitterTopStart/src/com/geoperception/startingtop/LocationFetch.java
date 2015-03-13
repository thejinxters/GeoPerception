package com.geoperception.startingtop;

import twitter4j.Place;
import twitter4j.Status;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class LocationFetch extends BaseBasicBolt{

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		Status status = (Status) tuple.getValueByField("tweet");
		Place place = (Place) status.getPlace();
		if (place != null){
			String toE = (String) place.getName() + "," + (String) place.getCountry();
			collector.emit(new Values(toE));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("place"));
	}

}
