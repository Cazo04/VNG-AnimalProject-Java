package com.example.staff.dto;

public record StaffCreatedRequestDto(
    String code,
    String fullName,
    String email,
    String role,
    String status
) {
}
