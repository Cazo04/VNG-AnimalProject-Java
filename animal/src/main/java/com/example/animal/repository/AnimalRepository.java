package com.example.animal.repository;

import org.springframework.stereotype.Repository;
import com.example.animal.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // Repository methods for Animal entity
}