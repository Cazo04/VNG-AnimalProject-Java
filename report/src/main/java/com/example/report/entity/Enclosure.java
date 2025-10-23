package com.example.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "enclosure")
public class Enclosure {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    
    @Column(name = "location", length = 200)
    private String location;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
    @Column(name = "current_animal_count")
    private Integer currentAnimalCount;

    public Enclosure() {
    }

    public Enclosure(Long id, String name, String type, String location, Integer capacity, Integer currentAnimalCount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.currentAnimalCount = currentAnimalCount;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentAnimalCount() {
        return currentAnimalCount;
    }

    public void setCurrentAnimalCount(Integer currentAnimalCount) {
        this.currentAnimalCount = currentAnimalCount;
    }
}
