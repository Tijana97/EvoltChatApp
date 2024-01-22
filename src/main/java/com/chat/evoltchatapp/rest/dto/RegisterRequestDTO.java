package com.chat.evoltchatapp.rest.dto;

import com.chat.evoltchatapp.core.model.User;

public class RegisterRequestDTO {
    String email;
    String password;

    public RegisterRequestDTO(){}

    public RegisterRequestDTO(User user){
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
