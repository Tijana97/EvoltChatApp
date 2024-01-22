package com.chat.evoltchatapp.rest.dto;

import com.chat.evoltchatapp.core.model.User;

public class UserDTO {

    private String id;

    private String username;

    public UserDTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
