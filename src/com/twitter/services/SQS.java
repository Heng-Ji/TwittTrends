package com.twitter.services;
/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;

/**
 * This sample demonstrates how to make basic requests to Amazon SQS using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web
 * Services developer account, and be signed up to use Amazon SQS. For more
 * information on Amazon SQS, see http://aws.amazon.com/sqs.
 * <p>
 * Fill in your AWS access credentials in the provided credentials file
 * template, and be sure to move the file to the default location
 * (/Users/hengji/.aws/credentials) where the sample code will load the credentials from.
 * <p>
 * <b>WARNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 */
public class SQS {
	private static AmazonSQS sqs;
	private static String myQueueUrl = "";
	private final static Logger logger = Logger.getLogger(Class.class);
	private static void createSQS(){
		AWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials("", "");
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs = new AmazonSQSClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);
	}
	
	public static void createQueue(String queueName) {
		try {
            // Create a queue
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
            myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();

            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();
        }
        catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String msg){
		// Send a message
		
		if (sqs == null || myQueueUrl.isEmpty()) {
			createSQS();
		}
        sqs.sendMessage(new SendMessageRequest(myQueueUrl, msg));
	}
	public static Message receiveMessage(){
		// Receive a message
		
		if (sqs == null || myQueueUrl.isEmpty()) {
			createSQS();
		}

		ReceiveMessageRequest request = new ReceiveMessageRequest(myQueueUrl);
		request.setMaxNumberOfMessages(1);
		ReceiveMessageResult result = sqs.receiveMessage(request);

		if (result != null && result.getMessages().size() > 0) {
			return (Message)result.getMessages().get(0);
		}

		return null;
		
        
	}
	
	public static void deleteMessage(Message message){
		// Delete a message
		
			DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(myQueueUrl, message.getReceiptHandle());			
			sqs.deleteMessage(deleteMessageRequest);
			logger.info("successfully delete" + deleteMessageRequest.toString());
	}
	
	public static void deleteQueue(){
		// Delete the queue
		
		try{
            sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
