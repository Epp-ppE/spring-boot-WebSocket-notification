package com.eppe.springwebsocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // check if this id is already connecected

        registry.addHandler(new MessageWebSocketHandler(), "/ws/message/{id}")
                .addInterceptors(new WebSocketHandshakeInterceptor())
                .setAllowedOrigins("*");

    }
}
