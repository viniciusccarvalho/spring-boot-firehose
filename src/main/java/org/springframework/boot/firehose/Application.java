package org.springframework.boot.firehose;

import org.cloudfoundry.client.lib.oauth2.OauthClient;
import org.cloudfoundry.client.lib.util.RestUtil;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;

import java.net.URL;

/**
 * Created by vcarvalho on 4/20/15.
 */

@SpringBootApplication
@ImportResource(value={"classpath:/metric-flow.xml"})
public class Application {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

    @Bean
    public AsyncRestTemplate asyncTemplate() throws Exception{
        return new AsyncRestTemplate();
    }

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



}
