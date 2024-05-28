package com.eppe.springwebsocket;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        
        String id = getIdFromUri(request.getURI().getPath());

        // Check if the ID is already in the connection map
        if (MessageWebSocketHandler.connectionMap.containsKey(id)) {
            // Reject the handshake by setting the response status to 403 Forbidden
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false; // Return false to indicate the handshake should not proceed
        }

        attributes.put("id", id);

        // Allow the handshake to proceed
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
    }

    private String getIdFromUri(String uri) {
        // Assuming the URI pattern is /ws/message/{id}
        String[] parts = uri.split("/");
        return parts[parts.length - 1]; // Extract the ID from the last part of the path
    }
}