package com.example.auth.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCreatedCommand {
    private String requestId;
    private String username;
    private String email;
    private String password;
    private String role;
}
