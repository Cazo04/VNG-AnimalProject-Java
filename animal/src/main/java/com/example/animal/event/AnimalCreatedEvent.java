package com.example.animal.event;

import java.time.LocalDate;

public class AnimalCreatedEvent {
    private String name;
    private String species;
    private String gender;
    private Integer age;
    private LocalDate arrivalDate;
    private Long enclosureId;
    private Long animalId; // Database ID after persistence

    public AnimalCreatedEvent(String name, String species, String gender, Integer age, LocalDate arrivalDate, Long enclosureId) {
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.age = age;
        this.arrivalDate = arrivalDate;
        this.enclosureId = enclosureId;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public Long getEnclosureId() {
        return enclosureId;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }
}
