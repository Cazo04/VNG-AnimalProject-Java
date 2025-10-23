package com.example.animal.event;

public class AnimalDeletedEvent {
    private Long animalId;

    public AnimalDeletedEvent(Long animalId) {
        this.animalId = animalId;
    }

    public Long getAnimalId() { return animalId; }
}
