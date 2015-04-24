package com.geoperception.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.json.simple.JSONObject;
import org.mortbay.util.ajax.JSON;
import twitter4j.HashtagEntity;
import twitter4j.Place;
import twitter4j.Status;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by johnluke on 4/9/15.
 */
public class ParseTweetDataBolt extends BaseBasicBolt {
    int writeCount;

    public ParseTweetDataBolt(){writeCount = 0;}

    @SuppressWarnings("unchecked")
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

            try{
                PrintWriter out = new PrintWriter("tweetData.out");
                JSONObject write = new JSONObject();
                write.put("id",Long.toString(id));
                write.put("content", content);
                write.put("username", uName);
                write.put("time", createdAt.toString());
                write.put("lat", Double.toString(lat));
                write.put("lng", Double.toString(lng));
                write.put("hashtags",hashTagText.toString());
                out.println(write);
//                String x = Long.toString(id) + ";" + content + ";" + uName + ";" + createdAt.toString() + ";"
//                        + Double.toString(lat) + ";" + Double.toString(lng) + ";" + hashTagText.toString();
                writeCount ++;
                if (writeCount == 5){
                    out.print("");
                    writeCount = 0;
                }
                out.close();
            }catch (IOException e) {
                //exception handling left as an exercise for the reader
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
