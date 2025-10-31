package com.example.animal.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;

import com.example.animal.config.ProcessorGroups;
import com.example.animal.event.AnimalDeletedEvent;
import com.example.animal.repository.AnimalRepository;
import com.example.animal.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.ANIMAL_DELETED)
public class AnimalDeletedEventHandler {

    private final AnimalRepository animalRepository;
    private final LoggingMQService loggingMQService;

    public AnimalDeletedEventHandler(AnimalRepository animalRepository, LoggingMQService loggingMQService) {
        this.animalRepository = animalRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(AnimalDeletedEvent event, EventMessage<?> eventMessage) {
    System.out.println("Handling AnimalDeletedEvent: " + eventMessage.getIdentifier());

    long startTime = System.currentTimeMillis();
    loggingMQService.logEm(
        "Handling AnimalDeletedEvent",
        eventMessage.getIdentifier(),
        event.getRequestId(),
        ProcessorGroups.ANIMAL_DELETED);

    animalRepository.deleteById(event.getId());

    long duration = System.currentTimeMillis() - startTime;
    System.out.println("Animal deleted with ID: " + event.getId());
    loggingMQService.logEmWithDuration(
        "Animal deleted",
        eventMessage.getIdentifier(),
        event.getRequestId(),
        ProcessorGroups.ANIMAL_DELETED,
        duration);
    }
}