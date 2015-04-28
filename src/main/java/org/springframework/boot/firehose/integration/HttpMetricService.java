package org.springframework.boot.firehose.integration;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import events.EnvelopeOuterClass.Envelope;
import events.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class HttpMetricService {

    private final Logger logger = LoggerFactory.getLogger(HttpMetricService.class);

    @Autowired
    private MetricRegistry registry;





    public void onMessage(Message<Envelope> message){
        registerMetrics(message.getPayload());
    }


    private void registerMetrics(Envelope envelope){
        registerRequestMeters(envelope);
    }

    private void registerRequestMeters(Envelope envelope){
        registry.meter("requests."+envelope.getHttpStartStop().getStatusCode()).mark();
        registry.timer("response-time").update((envelope.getHttpStartStop().getStopTimestamp() - envelope.getHttpStartStop().getStartTimestamp()), TimeUnit.NANOSECONDS);
        registry.histogram("response-size").update(envelope.getHttpStartStop().getContentLength());
    }

}
