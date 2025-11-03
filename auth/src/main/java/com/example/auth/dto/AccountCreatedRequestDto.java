package com.example.auth.dto;

public record AccountCreatedRequestDto(
    String username,
    String email,
    String password,
    String role
) {
}
