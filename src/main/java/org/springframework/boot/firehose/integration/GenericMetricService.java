package org.springframework.boot.firehose.integration;

import events.EnvelopeOuterClass;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Created by vcarvalho on 4/22/15.
 */
@Component
public class GenericMetricService {
    public void onMessage(Message<EnvelopeOuterClass.Envelope> message){
        System.out.println(message.getPayload());
    }
}
