package com.example.enclosure.handler;

import org.axonframework.config.ProcessingGroup;
import org.springframework.stereotype.Service;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;

import com.example.enclosure.config.ProcessorGroups;
import com.example.enclosure.repository.EnclosureRepository;
import com.example.enclosure.service.LoggingMQService;
import com.example.enclosure.event.EnclosureUpdatedAnimalCountEvent;
import com.example.enclosure.entity.Enclosure;

@Service
@ProcessingGroup(ProcessorGroups.ENCLOSURE_UPDATED_ANIMAL_COUNT)
public class EnclosureUpdatedAnimalCountEventHandler {
    
    private final EnclosureRepository enclosureRepository;
    private final LoggingMQService loggingMQService;

    public EnclosureUpdatedAnimalCountEventHandler(EnclosureRepository enclosureRepository, LoggingMQService loggingMQService) {
        this.enclosureRepository = enclosureRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(EnclosureUpdatedAnimalCountEvent event, EventMessage<?> eventMessage) {
    System.out.println("Handling EnclosureUpdatedAnimalCountEvent: " + eventMessage.getIdentifier());

    long startTime = System.currentTimeMillis();
    loggingMQService.logEm(
        "Handling EnclosureUpdatedAnimalCountEvent",
        eventMessage.getIdentifier(),
        String.valueOf(event.getAnimalId()),
        ProcessorGroups.ENCLOSURE_UPDATED_ANIMAL_COUNT);

    Enclosure enclosure = enclosureRepository.findById(event.getEnclosureId())
        .orElseThrow(() -> new IllegalArgumentException("Enclosure not found"));
    // Increment animal count (or set as needed)
    Integer currentCount = enclosure.getCurrentAnimalCount();
    if (currentCount == null) {
        currentCount = 0;
    }
    enclosure.setCurrentAnimalCount(currentCount + 1); // Assumes event means add one animal
    enclosureRepository.save(enclosure);

    long duration = System.currentTimeMillis() - startTime;
    loggingMQService.logEmWithDuration(
        "Enclosure animal count updated",
        eventMessage.getIdentifier(),
        String.valueOf(event.getAnimalId()),
        ProcessorGroups.ENCLOSURE_UPDATED_ANIMAL_COUNT,
        duration);
    }
}
