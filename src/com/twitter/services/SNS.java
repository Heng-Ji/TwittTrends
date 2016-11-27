package com.twitter.services;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.ConfirmSubscriptionResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;

public class SNS {
	private static AmazonSNSClient sns;
	private static String topicArn = "";
	// private static String endpoint = "/topic/";

	private static void createSNS() {
		// create a new SNS client and set endpoint
		sns = new AmazonSNSClient(
				new BasicAWSCredentials("", ""));
		sns.setRegion(Region.getRegion(Regions.US_WEST_2));

		// create a new SNS topic
		// CreateTopicRequest createTopicRequest = new
		// CreateTopicRequest("tweetTrend");

	}

	public static void publish(String msg) {
		if (sns == null) {
			createSNS();
		}
		PublishRequest publishRequest = new PublishRequest(topicArn, msg);
		PublishResult publishResult = sns.publish(publishRequest);
		System.out.println("MessageId - " + publishResult.getMessageId());
	}

	// public static void subscribe() {
	// if(sns == null) {
	// createSNS();
	// }
	// SubscribeRequest subRequest = new SubscribeRequest(topicArn, "http",
	// endpoint);
	// sns.subscribe(subRequest);
	// // get request id for SubscribeRequest from SNS metadata
	// System.out.println("SubscribeRequest - " +
	// sns.getCachedResponseMetadata(subRequest));
	// }

	//
	// public static void confirmSubscribe(String topicArn, String token) {
	// if (sns == null) {
	// createSNS();
	// }
	// try {
	// ConfirmSubscriptionRequest confirmSubscriptionRequest = new
	// ConfirmSubscriptionRequest()
	// .withTopicArn(topicArn).withToken(token);
	// ConfirmSubscriptionResult resutlt =
	// sns.confirmSubscription(confirmSubscriptionRequest);
	// System.out.println("subscribed to " + resutlt.getSubscriptionArn());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public static void deleteTopic() {
	// DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
	// sns.deleteTopic(deleteTopicRequest);
	// // get request id for DeleteTopicRequest from SNS metadata
	// System.out.println("DeleteTopicRequest - " +
	// sns.getCachedResponseMetadata(deleteTopicRequest));
	// }

}
