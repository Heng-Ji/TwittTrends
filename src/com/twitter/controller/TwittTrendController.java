package com.twitter.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.twitter.model.SNSMessage;
import com.twitter.services.Tweets;
import com.twitter.services.getTweets;
import com.twitter.services.postTweets;
import com.twitter.services.workerpool;

@Controller
public class TwittTrendController {
	
	private final static Logger logger = Logger.getLogger(Class.class);

	@ResponseBody
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/keyword/{keyWord}", method = RequestMethod.GET)
	public String search(@PathVariable("keyWord") String keyWord) {
		JSONObject json = new JSONObject();
		try {
//			Tweets.cleanUp();
			Tweets.startStreaming(keyWord);
			workerpool.startWorking();;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		json.put("res", "success");
		return json.toString();
	}

	@SuppressWarnings("resource")
	@ResponseBody
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/topic", method = RequestMethod.POST)
	public void subscribe(HttpServletRequest request) {
		try {
			String messagetype = request.getHeader("x-amz-sns-message-type");
			if (messagetype == null)
				return;
			Scanner scan = new Scanner(request.getInputStream());
			StringBuilder builder = new StringBuilder();
			while (scan.hasNextLine()) {
				builder.append(scan.nextLine());
			}
			String message = builder.toString();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			SNSMessage msg = objectMapper.readValue(message, SNSMessage.class);
			if (messagetype.equals("SubscriptionConfirmation")) {

				// confirm subscription
				Scanner sc = new Scanner(new URL(msg.getSubscribeURL()).openStream());
				StringBuilder sb = new StringBuilder();
				while (sc.hasNextLine()) {
					sb.append(sc.nextLine());
				}
				logger.info(
						" >> Subscription confirmation (" + msg.getSubscribeURL() + ") Return value: " + sb.toString());
			} else if (messagetype.equals("Notification")) {

				// retrieve data
				String dataContent = msg.getMessage();

				// TODO : index data in elastic search
				logger.info(">> message is :" + msg.getMessage());
				JSONObject json = new JSONObject(dataContent);
				logger.info(">> message json is :" + json.toString());
				postTweets p = new postTweets();
				try {
					p.sendPost(json);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/display/{keyWord}", method = RequestMethod.GET)
	public String result(@PathVariable("keyWord") String keyWord) {

		// get data from elastic search and display in front-end (via http
		// request)
		JSONArray json = new JSONArray();
		try {
			json = getTweets.sendGet(keyWord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		logger.info("retrieved data: " + json.toString());
		return json.toString();
	}
}
