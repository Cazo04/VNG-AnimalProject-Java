package com.example.health.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "health")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Health {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "animal_id")
    private Long animalId;

    @Column(name = "staff_id")
    private Long staffId;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "activity", columnDefinition = "TEXT")
    private String activity;

    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime;
}
