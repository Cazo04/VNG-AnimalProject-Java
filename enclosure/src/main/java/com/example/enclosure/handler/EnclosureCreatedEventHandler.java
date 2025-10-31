package com.example.enclosure.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Service;

import com.example.enclosure.config.ProcessorGroups;
import com.example.enclosure.entity.Enclosure;
import com.example.enclosure.event.EnclosureCreatedEvent;
import com.example.enclosure.repository.EnclosureRepository;
import com.example.enclosure.service.LoggingMQService;

@Service
@ProcessingGroup(ProcessorGroups.ENCLOSURE_CREATED)
public class EnclosureCreatedEventHandler {

    private final EnclosureRepository enclosureRepository;
    private final LoggingMQService loggingMQService;

    public EnclosureCreatedEventHandler(EnclosureRepository enclosureRepository, LoggingMQService loggingMQService) {
        this.enclosureRepository = enclosureRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(EnclosureCreatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling EnclosureCreatedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling EnclosureCreatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ENCLOSURE_CREATED);

        Enclosure enclosure = new Enclosure(
                null,
                event.getName(),
                event.getType(),
                event.getLocation(),
                event.getCapacity(),
                event.getCurrentAnimalCount()
        );
        Enclosure savedEnclosure = enclosureRepository.save(enclosure);

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Enclosure created with ID: " + savedEnclosure.getId());
        loggingMQService.logEmWithDuration(
                "Enclosure created",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ENCLOSURE_CREATED,
                duration);
    }

}