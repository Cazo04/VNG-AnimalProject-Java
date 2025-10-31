package com.example.enclosure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnclosureDeletedEvent {
    private String requestId;
    private Long id;
}