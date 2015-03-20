package com.geoperception.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class GeoCoderator extends BaseBasicBolt{
	
	String Okey;
	
	public GeoCoderator(String Okey){
		this.Okey = Okey;
	}
	
	public void makeRequest(String req){
		
	}

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String place = (String) tuple.getValueByField("place");
		String req = "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ place + "&key=" + Okey;
		makeRequest(req);
	}
	
	

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

}
