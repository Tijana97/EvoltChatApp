package com.chat.evoltchatapp.core.service;

import com.chat.evoltchatapp.core.model.User;
import com.chat.evoltchatapp.core.repository.UserRepository;
import com.chat.evoltchatapp.rest.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserDTO getUserByEmail(String email) throws Exception {
        Optional<User> user = userRepository.findFirstByEmail(email);
        if(user.isPresent()){
            return new UserDTO(user.get());
        } else{
            throw  new Exception("User with that email not found");
        }

    }

    public boolean usernameExists(String username) {
        Optional<User> user = userRepository.findFirstByUsername(username);
        return user.isPresent();
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                Optional<User> user = userRepository.findFirstByUsername(username);
                if(user.isPresent()){
                    return user.get();
                } else {
                    Optional<User> user2 = userRepository.findFirstByEmail(username);
                    if(user2.isPresent()){
                        return  user2.get();
                    } else{
                        throw  new UsernameNotFoundException("User not found.");
                    }
                }
            }
        };
    }

    public UserDTO addUser(User user) throws Exception {
        Optional<User> tempUser = userRepository.findFirstByEmail(user.getEmail());
        if(tempUser.isPresent()){
            throw new Exception("Email Already in DB.");
        } else{
            return new UserDTO(userRepository.save(user));
        }
    }

    public User findById(String userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        } else{
            throw  new Exception("User with that email not found");
        }
    }
}
