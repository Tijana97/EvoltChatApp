package com.chat.evoltchatapp.rest.websockets;

import com.chat.evoltchatapp.core.model.User;
import com.chat.evoltchatapp.core.service.JWTService;
import com.chat.evoltchatapp.core.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MainSocketHandler implements WebSocketHandler {

    private final JWTService jwtService;
    private final UserService userService;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public MainSocketHandler(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uri = session.getUri().toString();
        System.out.println("URI: " + uri);
        String token =  uri.substring(36);
        System.out.println("Token: " + token);
        User user = getUser(token);
        System.out.println("MY user: " + session);
        if (user == null) {
            System.out.println("User is null.");
            session.close();
            return;
        }

        sessions.put(user.getId(), session);
        System.out.println("Session created for the user " + user.getUsername() + " where the session id is " + session.getId());
        broadcastConnectedUsers(); // Notify all clients about the updated list of connected users
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error happened " + session.getId() + " with reason ### " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        User disconnectedUser = findUserBySession(session);
        if (disconnectedUser != null) {
            sessions.remove(disconnectedUser.getId());
            System.out.println("Connection closed for user " + disconnectedUser.getId() + " with status ### " + closeStatus.getReason());
            broadcastConnectedUsers(); // Notify all clients about the updated list of connected users
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String messageReceived = (String) message.getPayload();
        System.out.println("Message received: " + messageReceived);
    }

    public User getUser(String token) throws IOException {


        String userEmail = jwtService.extractUserName(token);
        System.out.println("User Email: " + userEmail);

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
        if (userDetails instanceof User) {
            return (User) userDetails;
        } else {
            System.out.println("UserDetails is not an instance of User");
            return null;
        }
    }


    private User findUserBySession(WebSocketSession session) {
        return sessions.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(session))
                .map(entry -> {
                    try {
                        return userService.findById(entry.getKey());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(userDetails -> userDetails instanceof User)
                .map(userDetails -> (User) userDetails)
                .findFirst()
                .orElse(null);
    }
    public List<User> getConnectedUsers() throws Exception {
        List<String> userIds =  new ArrayList<>(sessions.keySet());
        List<User> users = new ArrayList<>();
        for (String userId: userIds) {
            User user = userService.findById(userId);
            users.add(user);

        }
        return users;
    }


    public void broadcastConnectedUsers() {
        try {
            List<String> connectedUsers = new ArrayList<>(sessions.keySet());
            List<String> usernames = new ArrayList<>();
            for (String userId: connectedUsers) {
                User user = userService.findById(userId);
                usernames.add(user.getUsername());
            }
            String usersJson = new ObjectMapper().writeValueAsString(usernames);
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("msgType", "new user join");
            jsonMap.put("values", usersJson);
            String jsonString = new ObjectMapper().writeValueAsString(jsonMap);
            TextMessage message = new TextMessage(jsonString);

            sessions.forEach((userId, userSession) -> {
                System.out.println("Ovdje " + userId);
                try {
                    if (userSession.isOpen()) {
                        userSession.sendMessage(message);
                        System.out.println("MSG: "+ message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public  void BroadcastNewMessage(){
        try {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("msgType", "new message added");
            String jsonString = new ObjectMapper().writeValueAsString(jsonMap);
            TextMessage message = new TextMessage(jsonString);
            sessions.forEach((userId, userSession) -> {
                try {
                    if (userSession.isOpen()) {
                        userSession.sendMessage(message);
                        System.out.println("MSG: "+ message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
