package com.example.auth.event;

import lombok.Getter;

@Getter
public class AccountCreatedEvent {
    private String requestId;
    private String username;
    private String email;
    private String password;
    private String role;
    private Long accountId; // Database ID after persistence

    public AccountCreatedEvent(String requestId, String username, String email, String password, String role) {
        this.requestId = requestId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
