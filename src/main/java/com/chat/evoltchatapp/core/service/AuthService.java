package com.chat.evoltchatapp.core.service;

import com.chat.evoltchatapp.core.model.User;
import com.chat.evoltchatapp.core.repository.UserRepository;
import com.chat.evoltchatapp.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO signUp(RegisterRequestDTO registerRequestDTO) {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail(registerRequestDTO.getEmail());

        String[] adjectives = {"chaotic", "delicious", "rotten", "hot", "sweet", "spicy", "mysterious", "hungry", "honest", "dry"};
        String[] foods = {"Peanut", "Banana", "Cherry", "Cake", "Burger", "Pizza", "Orange", "Apple", "Bread", "Steak"};

        String username = generateUniqueUsername(adjectives, foods);

        userRequestDTO.setUsername(username);
        userRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        User newUser = userRepository.save(userRequestDTO.toEntity());

        return new UserDTO(newUser);
    }

    private String generateUniqueUsername(String[] adjectives, String[] foods) {
        Random random = new Random();
        String username;
        do {
            String randomAdjective = adjectives[random.nextInt(adjectives.length)];
            String randomFood = foods[random.nextInt(foods.length)];
            int randomNumber = random.nextInt((9999 - 1000) + 1) + 1000;
            username = randomAdjective + randomFood + randomNumber;
        } while (userRepository.findFirstByUsername(username).isPresent());
        return username;
    }


    public LoginDTO signIn(LoginRequestDTO loginRequestDTO) throws Exception {
        System.out.println(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );
        System.out.println("EMail " + loginRequestDTO.getUsername());
        System.out.println("PW " + loginRequestDTO.getPassword());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Log or print exception details
            e.printStackTrace();
            throw e; // Rethrow the exception if needed
        }
        System.out.println("Authentication successful for: " + loginRequestDTO.getUsername());

        Optional<User> user = userRepository.findFirstByEmail(loginRequestDTO.getUsername());
        if(user.isPresent()){
            String jwt = jwtService.generateToken(user.get());
            System.out.println("TOKEN: " + jwt);

            return new LoginDTO(jwt);
        } else{
            throw new Exception("User does not exist.");
        }

    }

}

