package org.springframework.boot.firehose;


import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Reporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.firehose.annotations.EnableFirehoseMetrics;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;


/**
 * Created by vcarvalho on 4/20/15.
 */

@SpringBootApplication
@EnableFirehoseMetrics
public class Application {


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }








}
