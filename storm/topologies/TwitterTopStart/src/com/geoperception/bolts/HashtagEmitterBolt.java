package com.geoperception.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import twitter4j.HashtagEntity;

import java.util.List;

/**
 * Created by jinxters on 4/17/15.
 */
public class HashtagEmitterBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {

        Long tweetId = (Long) input.getValueByField("id");
        List<String> hashtags = (List<String>) input.getValueByField("hashTagText");

        for (String hashtag : hashtags){
            collector.emit(new Values(hashtag, tweetId));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("hashtag", "tweetId"));
    }
}
