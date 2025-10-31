package com.example.feeding.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedingCreatedCommand {

    private String requestId;
    private Long animalId;
    private String foodType;
    private BigDecimal quantity;
    private LocalDateTime feedingTime;
    private Long keeperId;
}
