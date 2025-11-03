package com.example.auth.dto;

public record LoginRequestDto(
    String username,
    String password
) {
}
