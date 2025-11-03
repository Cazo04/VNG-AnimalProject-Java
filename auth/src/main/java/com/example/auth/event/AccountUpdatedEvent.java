package com.example.auth.event;

import lombok.Getter;

@Getter
public class AccountUpdatedEvent {
    private String requestId;
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;

    public AccountUpdatedEvent(String requestId, Long id, String username, String email, String password, String role) {
        this.requestId = requestId;
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
