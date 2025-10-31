package com.example.enclosure.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnclosureUpdatedAnimalCountCommand {
    private Long animalId;
    private Long enclosureId;
}
