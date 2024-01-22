package com.chat.evoltchatapp.rest.controller;

import com.chat.evoltchatapp.core.model.Message;
import com.chat.evoltchatapp.core.service.MessageService;
import com.chat.evoltchatapp.rest.dto.MessageDTO;
import com.chat.evoltchatapp.rest.websockets.MainSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class MessageController {

    private final MessageService messageService;
    private  final MainSocketHandler mainSocketHandler;

    public MessageController(MessageService messageService, MainSocketHandler mainSocketHandler){
        this.messageService = messageService;
        this.mainSocketHandler = mainSocketHandler;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<List<Message>> getMessages(){
        return ResponseEntity.ok(messageService.getMessages());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/new")
    public ResponseEntity<Message> addMessage(@RequestBody MessageDTO message) throws IOException {
        System.out.println(message.getMessage());
        System.out.println(message.getUsername());
        Message newMessage = messageService.addMessage(message);
        mainSocketHandler.BroadcastNewMessage();

        return  ResponseEntity.ok(newMessage);
    }



}
