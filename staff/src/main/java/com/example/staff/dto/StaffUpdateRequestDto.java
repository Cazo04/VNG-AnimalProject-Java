package com.example.staff.dto;

public record StaffUpdateRequestDto(
    String code,
    String fullName,
    String email,
    String role,
    String status
) {}
