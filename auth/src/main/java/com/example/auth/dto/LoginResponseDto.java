package com.example.auth.dto;

public record LoginResponseDto(
    String token,
    String username,
    String role
) {
}
