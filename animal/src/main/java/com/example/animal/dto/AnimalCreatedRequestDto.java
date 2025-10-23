package com.example.animal.dto;

import java.time.LocalDate;

public record AnimalCreatedRequestDto(
    String name,
    String species,
    String gender,
    Integer age,
    LocalDate arrivalDate,
    Long enclosureId
) {
}
