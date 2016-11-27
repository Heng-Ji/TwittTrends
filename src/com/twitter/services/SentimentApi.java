package com.twitter.services;

import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
//import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
//import com.ibm.watson.developer_cloud.alchemy.v1.model.Sentiment;
//import com.ibm.watson.developer_cloud.alchemy.v1.model.TypedRelations;

public class SentimentApi {
	public static String getSentiment(String text) {
		String apiKey = "";
//		String apiKey = "";
		AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey(apiKey);
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put(AlchemyLanguage.TEXT, text);

	    // get sentiment
	    DocumentSentiment sentiment = service.getSentiment(params).execute();
	    return sentiment.getSentiment().getType().toString();
	}
}
