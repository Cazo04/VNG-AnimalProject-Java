package com.example.enclosure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
