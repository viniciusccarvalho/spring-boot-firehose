package org.springframework.boot.firehose.model;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by vcarvalho on 4/20/15.
 */
@Component
public class Stats {

    private AtomicLong totalEvents = new AtomicLong(1L);
    private AtomicLong counterEvents = new AtomicLong(1L);
    private AtomicLong metricEvents = new AtomicLong(1L);
    private final Long startedTime;

    public Stats(){
        this.startedTime = System.currentTimeMillis();
    }


    public void increaseMetrics(){
        this.totalEvents.incrementAndGet();
        this.metricEvents.incrementAndGet();
    }

    public void increaseCounter(){
        this.totalEvents.incrementAndGet();
        this.counterEvents.incrementAndGet();
    }

    public AtomicLong getTotalEvents() {
        return totalEvents;
    }

    public AtomicLong getCounterEvents() {
        return counterEvents;
    }

    public AtomicLong getMetricEvents() {
        return metricEvents;
    }

    public Long getStartedTime() {
        return startedTime;
    }
}
