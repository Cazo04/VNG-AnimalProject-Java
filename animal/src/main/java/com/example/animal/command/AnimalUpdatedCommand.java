package com.example.animal.command;

import java.time.LocalDate;

public class AnimalUpdatedCommand {
    private Long id;
    private String name;
    private String species;
    private String gender;
    private Integer age;
    private LocalDate arrivalDate;
    private Long enclosureId;

    public AnimalUpdatedCommand(Long id, String name, String species, String gender, Integer age, LocalDate arrivalDate, Long enclosureId) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.age = age;
        this.arrivalDate = arrivalDate;
        this.enclosureId = enclosureId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getGender() { return gender; }
    public Integer getAge() { return age; }
    public LocalDate getArrivalDate() { return arrivalDate; }
    public Long getEnclosureId() { return enclosureId; }
}
