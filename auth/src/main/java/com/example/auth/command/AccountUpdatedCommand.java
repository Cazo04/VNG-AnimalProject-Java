package com.example.auth.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountUpdatedCommand {
    private String requestId;
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
}
