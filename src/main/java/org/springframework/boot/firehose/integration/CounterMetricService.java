package org.springframework.boot.firehose.integration;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import events.EnvelopeOuterClass.Envelope;
import events.Metric.CounterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Created by vcarvalho on 4/22/15.
 */
@Component
public class CounterMetricService {

    @Autowired
    private MetricRegistry registry;

    public void onMessage(Message<Envelope> message){
        CounterEvent event = message.getPayload().getCounterEvent();
        Counter counter = registry.counter(message.getPayload().getOrigin()+"."+event.getName());
        counter.inc(event.getDelta());

    }

}
