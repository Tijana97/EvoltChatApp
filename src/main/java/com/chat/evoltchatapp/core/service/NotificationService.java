package com.chat.evoltchatapp.core.service;

import com.chat.evoltchatapp.rest.websockets.MainSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Service
public class NotificationService {
    private final MainSocketHandler mainSocketHandler;


    public NotificationService(MainSocketHandler mainSocketHandler) {
        this.mainSocketHandler = mainSocketHandler;
    }








}

