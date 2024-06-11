package com.eppe.springwebsocket;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

@RestController
public class WebSocketController {

    @GetMapping("/ws/users")
    public List<String> getWsConnections() {
        return MessageWebSocketHandler.connectionMap.keySet().stream().collect(Collectors.toList());
    }

    @PostMapping("/ws/broadcast")
    public void broadcastWsMessage(@RequestBody RequestMessage req) {
        MessageWebSocketHandler.connectionMap.entrySet().forEach(entry -> {
            var connection = entry.getValue(); // Value (WebSocketSession)
            try {
                connection.sendMessage(new TextMessage(req.message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @PostMapping("/ws/publish")
    public ResponseEntity<String> publishWsMessage(@RequestBody RequestMessage req) throws Exception {
        var connection = MessageWebSocketHandler.connectionMap.get(req.toUserId);

        if (connection == null)
            return new ResponseEntity<String>("This user is not connected.", null, 404);

        connection.sendMessage(new TextMessage(req.message));

        return new ResponseEntity<String>("Message sent", null, 200);
    }
}