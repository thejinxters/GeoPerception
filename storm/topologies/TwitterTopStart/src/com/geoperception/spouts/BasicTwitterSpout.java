package com.geoperception.spouts;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;


@SuppressWarnings("serial")
public class BasicTwitterSpout extends BaseRichSpout{
	
	SpoutOutputCollector _collector;
	String consumerKey;
	String consumerSecret;
	String accessToken;
	String accessTokenSecret;
	String [] keyWords;
	LinkedBlockingQueue<Status> queue = null;
	TwitterStream _twitterStream;
	
	/***
	 * Creates a spout using the twitter API.
	 * @param consumerKey
	 * @param consumerSecret
	 * @param accessToken
	 * @param accessTokenSecret
	 * @param keyWords
	 */
	public BasicTwitterSpout(String consumerKey, String consumerSecret,
			String accessToken, String accessTokenSecret, String[] keyWords){
			this.consumerKey = consumerKey;
			this.consumerSecret = consumerSecret;
			this.accessToken = accessToken;
			this.accessTokenSecret = accessTokenSecret;
			this.keyWords = keyWords;
	}
	
	public BasicTwitterSpout(){
		
	}
	
	/***
	 * emits the next tuple from the queue
	 */
	@Override
	public void nextTuple() {
		Status ret = queue.poll();
		if (ret == null){
			Utils.sleep(50);
		} else{
			_collector.emit(new Values(ret));
		}
		
	}
	
	/***
	 * closes the twitter stream
	 */
	@Override
	public void close(){
		_twitterStream.shutdown();
	}
	
	/***
	 * returns the component configuration
	 */
	@Override
	public Map<String,Object> getComponentConfiguration(){
		Config ret = new Config();
		ret.setMaxTaskParallelism(1);
		return ret;
	}

	@Override
	/***
	 * opens the twitter stream for usage.
	 * the status listener listens for a status, and then places that status in the queue
	 * the twitter stream is also created
	 */
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		queue = new LinkedBlockingQueue<Status>();
		_collector = collector;
		
		StatusListener listener = new StatusListener(){

			@Override
			public void onException(Exception arg0) {
				// TODO Figure out other implemented methods
				
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Figure out other implemented methods
				
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Figure out other implemented methods
				
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Figure out other implemented methods
				
			}

			@Override
			public void onStatus(twitter4j.Status status) {
				queue.offer(status);
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Figure out other implemented methods
				
			}
			
		};
		
		_twitterStream = new TwitterStreamFactory(
				new ConfigurationBuilder().setJSONStoreEnabled(true).build())
				.getInstance();
		_twitterStream.addListener(listener);
		_twitterStream.setOAuthConsumer(consumerKey, consumerSecret);
		AccessToken token = new AccessToken(accessToken, accessTokenSecret);
		_twitterStream.setOAuthAccessToken(token);
		
		if (keyWords.length == 0) {
			_twitterStream.sample();
		}
		
		else {
			FilterQuery query = new FilterQuery().track(keyWords);
			_twitterStream.filter(query);
		}
	}
	
	@Override
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
	}

	@Override
	/***
	 * declares the name of the fields emitted from spout
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet"));
	}

}
