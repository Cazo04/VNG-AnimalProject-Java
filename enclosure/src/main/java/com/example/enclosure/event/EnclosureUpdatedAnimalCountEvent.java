package com.example.enclosure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnclosureUpdatedAnimalCountEvent {
    private Long animalId;
    private Long enclosureId;
}
