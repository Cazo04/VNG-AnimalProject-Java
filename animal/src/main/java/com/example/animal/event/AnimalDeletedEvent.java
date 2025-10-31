package com.example.animal.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDeletedEvent {
    private String requestId;
    private Long id;
}