package com.example.feeding.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "feeding")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feeding {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "animal_id")
    private Long animalId;
    
    @Column(name = "food_type", nullable = false, length = 100)
    private String foodType;
    
    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @Column(name = "feeding_time", nullable = false)
    private LocalDateTime feedingTime;
    
    @Column(name = "keeper_id")
    private Long keeperId;
}
