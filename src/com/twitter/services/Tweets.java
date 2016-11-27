package com.twitter.services;

import twitter4j.FilterQuery;
//import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
//import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class Tweets {
	private static final String ckey = "ZlPgE73ivGdJAl3ROeNHc1RKJ";
	private static final String csecret = "s9FyEOMOmjckzmtfuwm9Iw6NGiFk8tMxmEYllxYzC1rMd4XDVe";
	private static final String atoken = "784480113459093504-izVnLvSy8MAE5e84tYD04WY1CCaKD4L";
	private static final String asecret = "nhQiQaRxFrUyNLtCOjeLO7wBVx9XcvSG8OqCAtg5RhqmJ";
	private final static Logger logger = Logger.getLogger(Class.class);
	private static ConfigurationBuilder cb;
	private static TwitterStream twitterStream;
	private static boolean first = true;
	private static StatusListener listener;
	private static FilterQuery fq;

	public static void startStreaming(String keyWord) {
		streaming();
		String[] keywordsArray = { keyWord };
		fq.track(keywordsArray);
		twitterStream.filter(fq);

	}

	public static void streaming() {
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(ckey);
		cb.setOAuthConsumerSecret(csecret);
		cb.setOAuthAccessToken(atoken);
		cb.setOAuthAccessTokenSecret(asecret);

		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		listener = new StatusListener() {

			@Override
			public void onStatus(Status status) {

				// gets Username
				String username = status.getUser().getScreenName();
				String geo = status.getGeoLocation().toString();
				double lag = status.getGeoLocation().getLatitude();
				double lon = status.getGeoLocation().getLongitude();
				String text = status.getText();
				String lang = status.getLang().toString();
				// System.out.println(status.toString());
				if (geo != null && lang.equals("en")) {
					// System.out.println(username);
					// System.out.println(lag);
					// System.out.println(lon);
					// System.out.println(text);
					// System.out.println(lang);
					JSONObject json = new JSONObject();
					json.put("username", username);
					json.put("lag", lag);
					json.put("lon", lon);
					json.put("text", text);
					try {
						SQS.sendMessage(json.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.info(e.getMessage());
					}
				}
			}

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

		};

		
		twitterStream.addListener(listener);
		fq = new FilterQuery();

	}

//	public static void cleanUp() {
//		if (twitterStream != null) {
//			twitterStream.cleanUp();
//			first = true;
//		}
//	}
}