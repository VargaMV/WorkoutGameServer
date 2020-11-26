package com.msh.WorkoutGameServer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/public", "/private", "/admin");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //registry.addEndpoint("/action").withSockJS();
        registry.addEndpoint("/action").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/action").setAllowedOrigins("*");
        registry.addEndpoint("/admin").withSockJS();
        registry.addEndpoint("/admin");

    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(2048 * 2048);
        registry.setSendBufferSizeLimit(2048 * 2048);
        registry.setSendTimeLimit(2048 * 2048);
    }

}
