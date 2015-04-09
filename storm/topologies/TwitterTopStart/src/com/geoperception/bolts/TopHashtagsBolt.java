package com.geoperception.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by johnluke on 4/9/15.
 */
public class TopHashtagsBolt extends BaseBasicBolt {

    Map<String,Integer> counts;

    public TopHashtagsBolt(){
        counts = new HashMap<String, Integer>();
    }
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {

        List<String> hashes = (List<String>) input.getValueByField("hashTagText");
        Long idO = (Long)input.getValueByField("id");

        long id = idO.longValue();

        ///************READFROMCASSANDRA***************

        for (String tag : hashes){
            Integer count = counts.get(tag);
            if (count == null){
                count = 0;
            }
            count++;
            counts.put(tag,count);
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
