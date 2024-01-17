package com.chat.evoltchatapp.rest.controller;

import com.chat.evoltchatapp.core.model.Message;
import com.chat.evoltchatapp.core.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<List<Message>> getMessages(){
        return ResponseEntity.ok(messageService.getMessages());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/new")
    public ResponseEntity<Message> addMessage(@RequestBody Message message){
        return  ResponseEntity.ok(messageService.addMessage(message));
    }



}
