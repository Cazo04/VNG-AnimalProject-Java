package com.example.animal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalCreatedRequestDto {
    private String name;
    private String species;
    private String gender;
    private Integer age;
    private Long enclosureId;
}