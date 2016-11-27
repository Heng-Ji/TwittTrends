package com.twitter.model;

public class Sentiment {
	private Mood mood;	
	
	public Sentiment(){
	}
	
	public Sentiment(Mood m) {
		this.mood = m;
	}
	
	public Mood getMood() {
		return mood;
	}
}
