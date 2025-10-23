package com.example.animal.dto;

public record AnimalCreatedEventDto(
    Long animalId,
    Long enclosureId
) {
}
