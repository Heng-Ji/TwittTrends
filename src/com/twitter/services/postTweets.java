package com.twitter.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

public class postTweets {
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final static Logger logger = Logger.getLogger(Class.class);
	// HTTP POST request
	public void sendPost(JSONObject json) throws Exception {

		String url = "https://search-ccbigdata-vflc3fpahmprze34myukffb7fq.us-west-2.es.amazonaws.com/tests/t";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");
//		con.connect();
//		String urlParameters = "C02G8416DRJM";
//		BufferedWriter out = 
//			    new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
//		Gson gson = new Gson();
//		String json = gson.toJson(urlParameters);
//		JSONObject tweets  = new JSONObject();
//		tweets.put("name", name);
//		tweets.put("geo", geo);
//		tweets.put("text", text);
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(json.toString());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		logger.info("response code is" + responseCode);
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
}
