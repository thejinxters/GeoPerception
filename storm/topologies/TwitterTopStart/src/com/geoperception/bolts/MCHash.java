package com.geoperception.bolts;

import java.util.HashMap;
import java.util.Map;

import com.geoperception.utils.TwoTup;
import twitter4j.HashtagEntity;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class MCHash extends BaseBasicBolt{
	
	private Map<String,Map<String,Integer>> hashTagCounter;
	private Map<String,TwoTup<String,Integer>> countryTags;

    public MCHash(){
        this.hashTagCounter = new HashMap<String, Map<String, Integer>>();
        this.countryTags = new HashMap<String, TwoTup<String, Integer>>();
    }

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String country = (String) tuple.getValueByField("country");
		HashtagEntity[] hashtags = (HashtagEntity[])tuple.getValueByField("hashtags");
		for (int i = 0; i < hashtags.length; i ++){
            //counts hashtag for country
			String tag = hashtags[i].getText();
			Map<String,Integer> chashes = hashTagCounter.get(country);
            if (chashes == null){
                chashes = new HashMap<String, Integer>();
                hashTagCounter.put(country,chashes);
            }
            Integer count = chashes.get(tag);
            if (count == null) count = 0;
            count ++;
            chashes.put(tag,count);

            //updates countryTags if a new most common hashtag is found
            TwoTup<String,Integer> current = countryTags.get(country);
            if (current == null){
                current = new TwoTup<String, Integer>(tag,count);
                countryTags.put(country,current);
            } else{
                if (count >= current.value){
                    current = new TwoTup<String, Integer>(tag,count);
                    countryTags.put(country,current);
                }
            }

            System.out.println(country + ' ' + current.key + ' ' + current.value);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
		
	}

}
