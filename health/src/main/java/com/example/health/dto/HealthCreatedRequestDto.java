
package com.example.health.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HealthCreatedRequestDto {
    private Long animalId;
    private Long staffId;
    private BigDecimal weight;
    private String status;
    private String activity;
    private LocalDateTime checkTime;
}
