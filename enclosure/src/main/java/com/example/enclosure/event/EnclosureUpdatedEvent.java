package com.example.enclosure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnclosureUpdatedEvent {
    private String requestId;
    private Long id;
    private String name;
    private String type;
    private String location;
    private Integer capacity;
    private Integer currentAnimalCount;
}