package org.springframework.boot.firehose.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import events.EnvelopeOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.firehose.model.Serie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by vcarvalho on 4/20/15.
 */
@Component
public class InfluxDBServiceActivator {

    @Autowired
    private ObjectMapper mapper;

    private Logger logger = LoggerFactory.getLogger(InfluxDBServiceActivator.class);

    @Autowired
    private AsyncRestTemplate template;

    @ServiceActivator(inputChannel = "aggregated")
    public void process(Message<?> message){
        List<EnvelopeOuterClass.Envelope> messages = (List<EnvelopeOuterClass.Envelope>) message.getPayload();
        Map<String,Serie> series = new HashMap<>();
        messages.stream().forEach(env -> createOrAppendSeries(env,series));
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.put("Authorization", Collections.singletonList("Basic " + Base64Utils.encodeToString("root:root".getBytes())));
            HttpEntity<?> entity = new HttpEntity<>(series.values(),headers);
            logger.info("Sending {} metrics to influx db", messages.size());
            ListenableFuture<ResponseEntity<String>> futureEntity = template.exchange("http://10.68.105.101:8086/db/metrics/series", HttpMethod.POST, entity, String.class);
            futureEntity.addCallback(s -> {
            }, e -> {e.printStackTrace();});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createOrAppendSeries(EnvelopeOuterClass.Envelope envelope, Map<String,Serie> series){
        Map<String,Object> flatten = flattenValue(envelope);
        if(series.get(flatten.get("name")) != null){
            series.get(flatten.get("name")).addPoint(new Object[]{flatten.get("time"),flatten.get("value")});
        }else{
            Serie serie = new Serie(flatten.get("name").toString(),"time","value");
            serie.addPoint(new Object[]{flatten.get("time"),flatten.get("value")});
            series.put(flatten.get("name").toString(),serie);
        }
    }

    private Map<String, Object> flattenValue(EnvelopeOuterClass.Envelope envelope){
        Map<String,Object> map = new HashMap<>();
        switch(envelope.getEventType()){
            case ValueMetric:
                map.put("name",envelope.getOrigin()+"."+envelope.getValueMetric().getName());
                map.put("value",envelope.getValueMetric().getValue());
                map.put("time",envelope.getTimestamp()/1000000);
            break;

            case CounterEvent:
                map.put("name",envelope.getOrigin()+"."+envelope.getCounterEvent().getName());
                map.put("value",envelope.getCounterEvent().getDelta());
                map.put("time",envelope.getTimestamp()/1000000);
                break;
            case ContainerMetric:
                System.out.println("#### container metric##### " + envelope.getContainerMetric());
                break;

        }
        return map;
    }
}
