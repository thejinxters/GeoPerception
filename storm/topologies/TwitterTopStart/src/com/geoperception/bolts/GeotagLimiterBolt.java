package com.geoperception.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import twitter4j.GeoLocation;
import twitter4j.Status;

/**
 * Created by johnluke on 4/9/15.
 */
public class GeotagLimiterBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {

        Status status = (Status) input.getValueByField("tweet");
        GeoLocation loc = status.getGeoLocation();
        if (loc != null){
            collector.emit(new Values(status));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("geotweet"));

    }
}
