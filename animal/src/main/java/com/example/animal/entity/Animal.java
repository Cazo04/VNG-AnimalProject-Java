package com.example.animal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "animal")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDateTime arrivalDate;
    
    @Column(name = "enclosure_id")
    private Long enclosureId;
}
