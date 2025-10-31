package com.example.animal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalUpdateRequestDto {
    private Long id;
    private String name;
    private String species;
    private String gender;
    private Integer age; 
    private LocalDateTime arrivalDate;
    private Long enclosureId;
}