package com.example.enclosure.dto;

public record EnclosureCreatedRequestDto(
    String name,
    String type,
    String location,
    int capacity
) {
}