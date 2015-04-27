package org.springframework.boot.firehose.integration.test;

import org.springframework.messaging.Message;

public class MessageLogger {
	
	public void onMessage(Message<?> message){
		System.out.println(message);
	}
	
}
