package org.springframework.boot.firehose.integration;

import events.EnvelopeOuterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.firehose.model.Stats;
import org.springframework.integration.annotation.Filter;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static events.EnvelopeOuterClass.Envelope.EventType.*;
/**
 * Created by vcarvalho on 4/20/15.
 */
@Component
public class EventFilter {

    @Autowired
    private Stats stats;

    @Filter(inputChannel = "output", outputChannel = "filtered" )
    public boolean accept(Message<?> message){
        EnvelopeOuterClass.Envelope envelope = (EnvelopeOuterClass.Envelope) message.getPayload();
        EnvelopeOuterClass.Envelope.EventType eventType = envelope.getEventType();
        switch(eventType){
            case ValueMetric:
                stats.increaseMetrics();
                break;
            case ContainerMetric:
                stats.increaseMetrics();
                break;
            case CounterEvent:
                stats.increaseCounter();
                break;
            case HttpStartStop:
                System.out.println(envelope.getHttpStartStop().getUri());
                System.out.println(envelope.getHttpStartStop().getApplicationId());
                break;
        }
        return (  eventType == CounterEvent ||
                  eventType == ContainerMetric ||
                  eventType == ValueMetric);
    }

}
