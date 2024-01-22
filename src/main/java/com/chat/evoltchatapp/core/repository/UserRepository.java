package com.chat.evoltchatapp.core.repository;

import com.chat.evoltchatapp.core.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findFirstByEmail(String email);

    Optional<User> findFirstByUsername(String username);

    Optional<User> findFirstByUsernameOrEmail(String username, String email);
}
