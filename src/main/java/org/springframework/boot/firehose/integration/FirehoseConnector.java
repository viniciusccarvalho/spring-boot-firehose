package org.springframework.boot.firehose.integration;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.oauth2.OauthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.firehose.model.ServiceState;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter;
import org.springframework.stereotype.Component;

/**
 * Created by vcarvalho on 4/26/15.
 */
@Component
public class FirehoseConnector implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private ClientWebSocketContainer webSocketContainer;

    @Autowired
    private Environment env;

    @Autowired
    private WebSocketInboundChannelAdapter webSocketInboundChannelAdapter;


    private volatile ServiceState state;

    public FirehoseConnector(){
        this.state = ServiceState.STOPPED;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        try {
            oauthClient.init(new CloudCredentials(env.getProperty("firehose.authentication.user"), env.getProperty("firehose.authentication.password"), "cf"));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "bearer " + oauthClient.getToken().getValue());
            webSocketContainer.setHeaders(headers);
            this.state = ServiceState.RUNNING;
            webSocketContainer.start();
            webSocketInboundChannelAdapter.start();
        } catch (Exception e) {
            this.state = ServiceState.INVALID_CREDENTIALS;
            e.printStackTrace();
        }

    }
}
