package com.twitter.model;

import org.json.JSONObject;

public class Tweet {
	private String username;
	private double lon;
	private double lag;
	private String text;
	private String sentiment;
	
	public Tweet(String username, double lon, double lag, String text, String sentiment) {
		this.username = username;
		this.lon = lon;
		this.lag = lag;
		this.text = text;
		this.sentiment = sentiment; 
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public double getLang() {
		return lon;
	}
	
	public void setLang(double lon) {
		this.lon = lon;
	}
	
	public double getLag() {
		return lag;
	}
	
	public void setLag(double lag) {
		this.lag = lag;
	}
	
	public String gettext() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getSentiment() {
		return sentiment;
	}
	
	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
	
	public JSONObject toJsonObj() {
		JSONObject json = new JSONObject();
//		JSONObject res = new JSONObject();
		json.put("text", text);
		json.put("username", username);
		json.put("lon", lon);
		json.put("lag", lag);
		json.put("sentiment", sentiment);
//		res.put("tweet", json.toString());
		return json;
	}
}
