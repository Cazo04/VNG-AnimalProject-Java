package com.example.animal.handler;

import org.springframework.stereotype.Component;
import org.axonframework.queryhandling.QueryHandler;

import com.example.animal.repository.AnimalRepository;
import com.example.animal.query.GetAllAnimalQuery;
import com.example.animal.entity.Animal;

import java.util.List;

@Component
public class AnimalQueryHandler {
    
    private final AnimalRepository animalRepository;

    public AnimalQueryHandler(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @QueryHandler
    public List<Animal> handle(GetAllAnimalQuery query) {
        return animalRepository.findAll();
    }
}
