package com.geoperception.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@SuppressWarnings("serial")
public class GeoCoderator extends BaseBasicBolt{
	
	String authKey;
	
	public GeoCoderator(String authKey){
		this.authKey = authKey;
	}


	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String place = (String) tuple.getValueByField("place");

        GeoApiContext context = new GeoApiContext().setApiKey(this.authKey);
        GeocodingResult[] results;
        try {
            results = GeocodingApi.geocode(context, place).await();
        } catch (Exception e) {
            results = new GeocodingResult[0];
            e.printStackTrace();
        }

        System.out.println(results[0].geometry.location);

        double lat = results[0].geometry.location.lat;
        double lng = results[0].geometry.location.lng;

        collector.emit(new Values(lat,lng));
    }
	
	

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("latitude", "longitude"));
		
	}

}
