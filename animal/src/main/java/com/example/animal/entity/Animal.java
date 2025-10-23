package com.example.animal.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "animal")
public class Animal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "species", nullable = false, length = 100)
    private String species;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "age")
    private Integer age;
    
    @Column(name = "arrival_date")
    private LocalDate arrivalDate;
    
    @Column(name = "enclosure_id")
    private Long enclosureId;

    public Animal() {
    }

    public Animal(String name, String species, String gender, Integer age, LocalDate arrivalDate, Long enclosureId) {
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.age = age;
        this.arrivalDate = arrivalDate;
        this.enclosureId = enclosureId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Long getEnclosureId() {
        return enclosureId;
    }

    public void setEnclosureId(Long enclosureId) {
        this.enclosureId = enclosureId;
    }
}
