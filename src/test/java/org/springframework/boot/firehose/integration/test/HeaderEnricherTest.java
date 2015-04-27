package org.springframework.boot.firehose.integration.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import events.EnvelopeOuterClass.Envelope;
import events.EnvelopeOuterClass.Envelope.EventType;
import events.Http.HttpStartStop;
import events.Http.Method;
import events.Http.PeerType;
import events.Uuid.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/metric-flow.xml", "/integration-test.xml"})
public class HeaderEnricherTest {

	@Autowired
	private MessageChannel output;

	@Test
	public void checkHeaders(){
		Envelope envelope = Envelope.newBuilder().setOrigin("localhost").setTimestamp(System.currentTimeMillis())
									.setEventType(EventType.HttpStartStop)
									.setHttpStartStop(HttpStartStop.newBuilder()
											.setContentLength(200L)
											.setStartTimestamp(System.currentTimeMillis())
											.setStopTimestamp(System.currentTimeMillis()+100)
											.setPeerType(PeerType.Server)
											.setStatusCode(200)
											.setRemoteAddress("localhost")
											.setUserAgent("Gecko")
											.setRequestId(UUID.newBuilder().setHigh(1).setLow(1).build())
											.setMethod(Method.GET)
											.setUri("http://acme.com/info")
											.build()
											)
									.build();
		Message<Envelope> message = new GenericMessage<Envelope>(envelope);
		output.send(message);
		System.out.println("DONE");
	}
}
