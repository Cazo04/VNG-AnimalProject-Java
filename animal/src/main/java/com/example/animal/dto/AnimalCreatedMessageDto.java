package com.example.animal.dto;

import java.time.LocalDateTime;

public class AnimalCreatedMessageDto {
    private Long animalId;
    private Long enclosureId;
    private String name;
    private String species;
    private LocalDateTime timestamp;
    private String status;

    public AnimalCreatedMessageDto() {
    }

    public AnimalCreatedMessageDto(Long animalId, Long enclosureId, String name, String species, LocalDateTime timestamp, String status) {
        this.animalId = animalId;
        this.enclosureId = enclosureId;
        this.name = name;
        this.species = species;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public Long getEnclosureId() {
        return enclosureId;
    }

    public void setEnclosureId(Long enclosureId) {
        this.enclosureId = enclosureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
