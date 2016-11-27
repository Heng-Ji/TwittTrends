package com.twitter.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class getTweets {
	private final static String USER_AGENT = "Mozilla/5.0";
	private final static int size = 1000;
	private final static Logger logger = Logger.getLogger(Class.class);
	// HTTP GET request
	public static JSONArray sendGet(String keyword) throws Exception {

		String url = "";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		logger.info(responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = null;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		con.disconnect();
		JSONObject json = new JSONObject(response.toString()).getJSONObject("hits");
		JSONArray arr = json.getJSONArray("hits");
		JSONArray res = new JSONArray();
		for(int i = 0; i < arr.length(); i++) {
			JSONObject tem = arr.getJSONObject(i).getJSONObject("_source");
			res.put(tem);
		}
//		logger.info(res);
		return res;
	}
}
