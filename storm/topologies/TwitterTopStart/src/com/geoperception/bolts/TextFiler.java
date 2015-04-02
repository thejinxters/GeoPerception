package com.geoperception.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.io.*;

/**
 * Created by johnluke on 4/2/15.
 */
public class TextFiler extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Double lat = (Double) input.getValueByField("lat");
        Double lng = (Double) input.getValueByField("lng");

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("GeoCoord.out", true)));
            out.println(lat + "," + lng);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
