package com.example.report.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AnimalHealthCheckDto(
    Long id,
    String animalName,
    String species,
    String enclosure,
    String weight,
    String healthStatus,
    String activityLevel,
    String checkBy, // First name + (code)
    LocalDateTime checkTime //yyyy-MM-dd HH:mm
) {
}
