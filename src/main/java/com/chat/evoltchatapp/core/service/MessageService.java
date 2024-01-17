package com.chat.evoltchatapp.core.service;

import com.chat.evoltchatapp.core.model.Message;
import com.chat.evoltchatapp.core.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessages(){
        return messageRepository.findAll();
    }

    public Message addMessage(Message payload){
        return messageRepository.save(payload);
    }


}
