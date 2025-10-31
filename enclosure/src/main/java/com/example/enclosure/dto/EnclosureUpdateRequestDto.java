package com.example.enclosure.dto;

public record EnclosureUpdateRequestDto(
    String name,
    String type,
    String location,
    int capacity
) {
}