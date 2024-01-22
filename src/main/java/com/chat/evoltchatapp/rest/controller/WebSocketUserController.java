package com.chat.evoltchatapp.rest.controller;

import com.chat.evoltchatapp.core.model.User;
import com.chat.evoltchatapp.rest.websockets.MainSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/websocket/users")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class WebSocketUserController {

    private final MainSocketHandler mainSocketHandler;

    public WebSocketUserController(MainSocketHandler mainSocketHandler) {
        this.mainSocketHandler = mainSocketHandler;
    }

    @GetMapping
    public ResponseEntity<List<User>> getConnectedUsers() throws Exception {
        List<User> connectedUsers = mainSocketHandler.getConnectedUsers();
        return ResponseEntity.ok(connectedUsers);
    }
}
