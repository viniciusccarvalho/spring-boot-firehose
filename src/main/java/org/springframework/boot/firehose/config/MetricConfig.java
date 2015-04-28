package org.springframework.boot.firehose.config;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Reporter;
import org.cloudfoundry.client.lib.oauth2.OauthClient;
import org.cloudfoundry.client.lib.util.RestUtil;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by vcarvalho on 4/28/15.
 */
@ImportResource(value={"classpath:/metric-flow.xml"})
public class MetricConfig {


    @Autowired
    private Environment env;


    @Bean
    public WebSocketClient webSocketClient(){
        return new JettyWebSocketClient(new org.eclipse.jetty.websocket.client.WebSocketClient(new SslContextFactory(true)));
    }

    @Bean
    public ClientWebSocketContainer webSocketContainer(WebSocketClient client){
        ClientWebSocketContainer container = new ClientWebSocketContainer(client,env.getProperty("firehose.doppler.url")+"/firehose/firehose-x");
        container.setAutoStartup(false);
        container.setOrigin(env.getProperty("firehose.ws.origin","http://localhost"));
        return container;
    }

    @Bean
    public OauthClient oauthClient() throws Exception{
        return  new OauthClient(new URL(env.getProperty("firehose.authentication.url")),new RestUtil().createRestTemplate(null,true));
    }


    @Bean
    @ConditionalOnMissingBean
    public Reporter consoleReporter(MetricRegistry registry){
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build();
        reporter.start(5, TimeUnit.SECONDS);
        return reporter;
    }

}
