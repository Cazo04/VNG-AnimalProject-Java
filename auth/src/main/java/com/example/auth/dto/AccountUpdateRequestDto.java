package com.example.auth.dto;

public record AccountUpdateRequestDto(
    String username,
    String email,
    String password,
    String role
) {
}
