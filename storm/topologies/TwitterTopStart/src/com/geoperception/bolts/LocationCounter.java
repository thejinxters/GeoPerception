package com.geoperception.bolts;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class LocationCounter extends BaseBasicBolt{
	
	Map<String,Integer> counts;
	
	public LocationCounter(){
		counts = new HashMap<String,Integer>();
	}

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String place = (String) tuple.getValueByField("place");
		Integer count = counts.get(place);
		if (count == null)
			count = 0;
		count++;
		counts.put(place,count);
		collector.emit(new Values(place, count));
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("place", "count"));
	}

}
