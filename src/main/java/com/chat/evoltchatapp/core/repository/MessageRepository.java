package com.chat.evoltchatapp.core.repository;

import com.chat.evoltchatapp.core.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}
