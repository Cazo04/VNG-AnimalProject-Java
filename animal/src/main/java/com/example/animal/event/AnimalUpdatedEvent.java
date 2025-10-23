package com.example.animal.event;

import java.time.LocalDate;

public class AnimalUpdatedEvent {
    private Long animalId;
    private String name;
    private String species;
    private String gender;
    private Integer age;
    private LocalDate arrivalDate;
    private Long enclosureId;

    public AnimalUpdatedEvent(Long animalId, String name, String species, String gender, Integer age, LocalDate arrivalDate, Long enclosureId) {
        this.animalId = animalId;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.age = age;
        this.arrivalDate = arrivalDate;
        this.enclosureId = enclosureId;
    }

    public Long getAnimalId() { return animalId; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getGender() { return gender; }
    public Integer getAge() { return age; }
    public LocalDate getArrivalDate() { return arrivalDate; }
    public Long getEnclosureId() { return enclosureId; }
}
