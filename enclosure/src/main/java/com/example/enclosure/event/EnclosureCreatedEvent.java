package com.example.enclosure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnclosureCreatedEvent {
    private String requestId;
    private String name;
    private String type;
    private String location;
    private Integer capacity;
    private Integer currentAnimalCount;
}