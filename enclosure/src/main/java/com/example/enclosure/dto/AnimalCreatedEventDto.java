package com.example.enclosure.dto;

public record AnimalCreatedEventDto(
    Long animalId,
    Long enclosureId
) {
}
