package org.springframework.boot.firehose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * Created by vcarvalho on 4/20/15.
 */

@SpringBootApplication
@ImportResource(value={"classpath:/config/firehose.xml","classpath:/boot-firehose.xml"})
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

    @Bean
    public AsyncRestTemplate asyncTemplate() throws Exception{
        return new AsyncRestTemplate();
    }
}
