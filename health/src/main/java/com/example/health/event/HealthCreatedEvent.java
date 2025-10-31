
package com.example.health.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class HealthCreatedEvent {
    private String requestId;
    private Long animalId;
    private Long staffId;
    private BigDecimal weight;
    private String status;
    private String activity;
    private LocalDateTime checkTime;
}
