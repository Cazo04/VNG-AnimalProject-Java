package com.example.animal.handler;


import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;

import com.example.animal.config.ProcessorGroups;
import com.example.animal.entity.Animal;
import com.example.animal.event.AnimalUpdatedEvent;
import com.example.animal.repository.AnimalRepository;
import com.example.animal.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.ANIMAL_UPDATED)
public class AnimalUpdatedEventHandler {

    private final AnimalRepository animalRepository;
    private final LoggingMQService loggingMQService;

    public AnimalUpdatedEventHandler(AnimalRepository animalRepository, LoggingMQService loggingMQService) {
        this.animalRepository = animalRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(AnimalUpdatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling AnimalUpdatedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling AnimalUpdatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ANIMAL_UPDATED);

        Animal animal = animalRepository.findById(event.getId()).orElse(null);
        if (animal != null) {
        animal.setName(event.getName());
        animal.setSpecies(event.getSpecies());
        animal.setGender(event.getGender());
        animal.setAge(event.getAge());
        animal.setArrivalDate(event.getArrivalDate());
        animal.setEnclosureId(event.getEnclosureId());
        Animal updatedAnimal = animalRepository.save(animal);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Animal updated with ID: " + updatedAnimal.getId());
        loggingMQService.logEmWithDuration(
            "Animal updated",
            eventMessage.getIdentifier(),
            event.getRequestId(),
            ProcessorGroups.ANIMAL_UPDATED,
            duration);
        } else {
            System.out.println("Animal not found for update: " + event.getId());
            loggingMQService.logEm(
                "Animal not found for update",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ANIMAL_UPDATED);
        }
    }
}