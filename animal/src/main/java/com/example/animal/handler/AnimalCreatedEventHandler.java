package com.example.animal.handler;

import java.time.LocalDateTime;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;

import com.example.animal.config.ProcessorGroups;
import com.example.animal.entity.Animal;
import com.example.animal.event.AnimalCreatedEvent;
import com.example.animal.repository.AnimalRepository;
import com.example.animal.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.ANIMAL_CREATED)
public class AnimalCreatedEventHandler {

    private final AnimalRepository animalRepository;
    private final LoggingMQService loggingMQService;

    public AnimalCreatedEventHandler(AnimalRepository animalRepository, LoggingMQService loggingMQService) {
        this.animalRepository = animalRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(AnimalCreatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling AnimalCreatedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling AnimalCreatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ANIMAL_CREATED);

        Animal animal = new Animal(
            null,
            event.getName(),
            event.getSpecies(),
            event.getGender(),
            event.getAge(),
            LocalDateTime.now(),
            event.getEnclosureId()
        );
        Animal savedAnimal = animalRepository.save(animal);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Animal created with ID: " + savedAnimal.getId());
        loggingMQService.logEmWithDuration(
                "Animal created",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ANIMAL_CREATED,
                duration);
        loggingMQService.sendAnimalCreatedEvent(
            savedAnimal.getId(),
            savedAnimal.getEnclosureId()
        );
    }
}