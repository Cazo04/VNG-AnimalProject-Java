package com.example.animal.event;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalUpdatedEvent {
    private String requestId;
    private Long id;
    private String name;
    private String species;
    private String gender;
    private Integer age;
    private LocalDateTime arrivalDate;
    private Long enclosureId;
}