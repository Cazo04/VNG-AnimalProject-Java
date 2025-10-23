package com.example.animal.handler;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.example.animal.entity.Animal;
import com.example.animal.event.AnimalCreatedEvent;
import com.example.animal.event.AnimalUpdatedEvent;
import com.example.animal.event.AnimalDeletedEvent;
import com.example.animal.repository.AnimalRepository;
import com.example.animal.service.MessagingService;

@Component
public class AnimalEventHandler {
    
    private final AnimalRepository animalRepository;
    private final MessagingService messagingService;

    public AnimalEventHandler(AnimalRepository animalRepository, MessagingService messagingService) {
        this.animalRepository = animalRepository;
        this.messagingService = messagingService;
    }

    @EventHandler
    public void on(AnimalCreatedEvent event) {
        try {
            // Save to database
            Animal animal = new Animal(
                event.getName(),
                event.getSpecies(),
                event.getGender(),
                event.getAge(),
                event.getArrivalDate(),
                event.getEnclosureId()
            );
            Animal savedAnimal = animalRepository.save(animal);
            
            // Notify animal created and log success
            messagingService.sendAnimalCreated(savedAnimal.getId(), savedAnimal.getEnclosureId());
            messagingService.logDb("Animal Created", 200,
                "Animal ID: " + savedAnimal.getId() + ", Enclosure ID: " + savedAnimal.getEnclosureId());
            
        } catch (Exception e) {
            // Log error
            messagingService.logDbError("Animal Creation Failed", e);
            throw new RuntimeException("Failed to save animal", e);
        }
    }

    @EventHandler
    public void on(AnimalUpdatedEvent event) {
        try {
            Animal animal = animalRepository.findById(event.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + event.getAnimalId()));
            animal.setName(event.getName());
            animal.setSpecies(event.getSpecies());
            animal.setGender(event.getGender());
            animal.setAge(event.getAge());
            animal.setArrivalDate(event.getArrivalDate());
            animal.setEnclosureId(event.getEnclosureId());
            Animal saved = animalRepository.save(animal);

            messagingService.logDb("Animal Updated", 200,
                "Animal ID: " + saved.getId() + ", Enclosure ID: " + saved.getEnclosureId());
        } catch (Exception e) {
            messagingService.logDbError("Animal Update Failed", e);
            throw new RuntimeException("Failed to update animal", e);
        }
    }

    @EventHandler
    public void on(AnimalDeletedEvent event) {
        try {
            animalRepository.deleteById(event.getAnimalId());
            messagingService.logDb("Animal Deleted", 200, "Animal ID: " + event.getAnimalId());
        } catch (Exception e) {
            messagingService.logDbError("Animal Delete Failed", e);
            throw new RuntimeException("Failed to delete animal", e);
        }
    }
}
