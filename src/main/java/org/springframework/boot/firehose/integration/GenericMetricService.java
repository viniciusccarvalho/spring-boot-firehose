package org.springframework.boot.firehose.integration;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import events.EnvelopeOuterClass.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by vcarvalho on 4/22/15.
 */
@Component
public class GenericMetricService {

    private Map<String,Double> metricValues;

    private Lock lock = new ReentrantLock();

    public GenericMetricService() {
        this.metricValues = new ConcurrentHashMap<>();
    }

    @Autowired
    private MetricRegistry registry;

    public void onMessage(Message<Envelope> message){
        Envelope envelope = message.getPayload();
        String key = envelope.getOrigin()+"."+envelope.getValueMetric().getName();
        //TODO there has to be a better way of doing this, but the registry does not accept duplicate registration. For now I'm leaving
        //like this
        try {
            lock.tryLock(1, TimeUnit.MILLISECONDS);
            if(!metricValues.containsKey(key)){
                metricValues.put(key,envelope.getValueMetric().getValue());
                registry.register(key,new MapGauge(key));
            }else{
                metricValues.put(key,envelope.getValueMetric().getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }


    class MapGauge implements Gauge<Double>{
        private final String key;
        public MapGauge(String key){
            this.key = key;
        }
        @Override
        public Double getValue() {
            return GenericMetricService.this.metricValues.get(key);
        }
    }
}
