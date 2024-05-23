package com.eppe.springwebsocket;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MessageWebSocketHandler extends TextWebSocketHandler {
    public static final Map<String, WebSocketSession> connectionMap = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String responsePayload = "Empty message!";
        if (!payload.equals(null)){
            var userId = extractIdFromWebSocketUrl(session.getUri().getPath());
            responsePayload = userId + " said \"" + payload + "\"";
        }
        TextMessage responseMessage = new TextMessage(responsePayload);
        session.sendMessage(responseMessage);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var userId = extractIdFromWebSocketUrl(session.getUri().getPath());

        System.out.println("Connected: " + userId);
        //print time
        System.out.println("Time: " + LocalDateTime.now());
        connectionMap.put(userId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var userId = extractIdFromWebSocketUrl(session.getUri().getPath());

        System.out.println("Disconnected: " + userId);
        System.out.println("Status: " + status);
        connectionMap.remove(userId);
    }

    private String extractIdFromWebSocketUrl(String path) {
        // Extract the id from the WebSocket URL path
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }
}
