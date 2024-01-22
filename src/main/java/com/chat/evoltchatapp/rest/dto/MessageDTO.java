package com.chat.evoltchatapp.rest.dto;

public class MessageDTO {
    private String username;
    private String message;



    public MessageDTO() {}


    public MessageDTO(String username, String message) {
        this.username = username;
        this.message = message;

    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
