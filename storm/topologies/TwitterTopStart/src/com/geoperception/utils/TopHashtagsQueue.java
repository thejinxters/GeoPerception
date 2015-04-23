package com.geoperception.utils;

import com.datastax.driver.core.Row;
import org.apache.commons.lang.ObjectUtils;

import java.util.PriorityQueue;

/**
 * Created by jinxters on 4/18/15.
 */
public class TopHashtagsQueue {

    private static TopHashtagsQueue hashtagsQueue;
    private PriorityQueue<Row> queue = new PriorityQueue<Row>(200, Row.getLong("count"));

    private TopHashtagsQueue(){
    }

    public static TopHashtagsQueue getInstance(){
        if (hashtagsQueue == null){
            hashtagsQueue = new TopHashtagsQueue();
        }
        return hashtagsQueue;
    }




}
