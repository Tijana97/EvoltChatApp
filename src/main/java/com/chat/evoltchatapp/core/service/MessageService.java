package com.chat.evoltchatapp.core.service;

import com.chat.evoltchatapp.core.model.Message;
import com.chat.evoltchatapp.core.model.User;
import com.chat.evoltchatapp.core.repository.MessageRepository;
import com.chat.evoltchatapp.rest.dto.MessageDTO;
import com.chat.evoltchatapp.rest.websockets.MainSocketHandler;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MainSocketHandler mainSocketHandler;

    public MessageService(MessageRepository messageRepository, MainSocketHandler mainSocketHandler){

        this.messageRepository = messageRepository;
        this.mainSocketHandler = mainSocketHandler;
    }

    public List<Message> getMessages(){
        return messageRepository.findAll(Sort.by(Sort.Direction.ASC,"date"));
    }

    public Message addMessage(MessageDTO payload) throws IOException {
        User user = mainSocketHandler.getUser(payload.getUsername());
        Message message = new Message();
        message.setContent(payload.getMessage());
        message.setUsername(user.getUsername());
        message.setDate(new Date());

        return messageRepository.save(message);
    }

}
