package com.twitter.services;

//import java.io.Serializable;

import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import com.amazonaws.services.sqs.model.Message;
import com.twitter.model.Tweet;


public class workerpool implements Runnable {
	private final static Logger logger = Logger.getLogger(Class.class);
	private static ScheduledExecutorService executor = null;
	private int index;
	public workerpool(int index) {
		this.index = index;
	}
	public static void startWorking() {
		executor = Executors.newScheduledThreadPool(5);
		for (int i = 0; i < 5; i++) {
			executor.scheduleAtFixedRate(new workerpool(i), 1, 3, TimeUnit.SECONDS); 
			//scheduleAtFixedRate(TimerTask task, long delay, long period)
		}
	}

	@Override
	public void run() {
		Message msg = SQS.receiveMessage();
		if (msg != null) {
			// retrieve values from sqs
			logger.info("worker" + index + "message is:" + msg.getBody());
			JSONObject json = new JSONObject(msg.getBody());
			double lon = (double) json.get("lon");
			double lag = (double) json.get("lag");
			String text = json.get("text").toString();
			String username = json.get("username").toString();

			// get sentiment

			String sentiment = SentimentApi.getSentiment(text);
			Tweet tweet = new Tweet(username, lon, lag, text, sentiment);
			JSONObject res = tweet.toJsonObj();
			logger.info("before publish" + res.toString());
			SNS.publish(res.toString());
			SQS.deleteMessage(msg);
		}
	}
}
