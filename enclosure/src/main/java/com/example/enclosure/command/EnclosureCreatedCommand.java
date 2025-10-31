package com.example.enclosure.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnclosureCreatedCommand {
    private String requestId;
    private String name;
    private String type;
    private String location;
    private Integer capacity;
    private Integer currentAnimalCount;
}
