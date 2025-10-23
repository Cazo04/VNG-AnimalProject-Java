package com.example.report.dto;

import java.time.LocalDateTime;

public record DailyFeedingDto(
    Long id,
    String animalName,
    String species,
    String enclosure,
    String foodType,
    Double quantity,
    String keeper, // First name + (code)
    LocalDateTime feedingTime //yyyy-MM-dd HH:mm
) {
    
}
