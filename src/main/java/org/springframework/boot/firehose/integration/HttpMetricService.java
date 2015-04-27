package org.springframework.boot.firehose.integration;

import com.codahale.metrics.MetricRegistry;
import events.EnvelopeOuterClass.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class HttpMetricService {

    @Autowired
    private MetricRegistry registry;

    public void onMessage(Message<Envelope> message){
        System.out.println(message.getPayload());
    }

}
