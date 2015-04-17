package com.geoperception.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import ch.qos.logback.core.net.SyslogOutputStream;
import twitter4j.HashtagEntity;
import twitter4j.Place;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by johnluke on 4/9/15.
 */
public class ParseTweetDataBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            Status status = (Status) input.getValueByField("geotweet");
            long id = status.getId();
            String content = status.getText();
            String uName = status.getUser().getScreenName();
            Date createdAt = status.getCreatedAt();
            double lat = status.getGeoLocation().getLatitude();
            double lng = status.getGeoLocation().getLongitude();
            Place place = status.getPlace();
            String city;
            if (place != null) {
                city = status.getPlace().getName();
            } else {
                city = "";
            }
            HashtagEntity[] hashtags = status.getHashtagEntities();

            List<String> hashTagText = new ArrayList<String>();
            for (int i = 0; i < hashtags.length; i++) {
                hashTagText.add(i, hashtags[i].getText());
            }
            collector.emit(new Values(id,content,uName, createdAt, lat,lng,city,hashTagText));
        } catch(Exception e){
            System.out.println("Data Missing");
            System.out.println(e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "content", "userName", "createdAt", "lat", "lng", "city", "hashTagText"));
    }
}