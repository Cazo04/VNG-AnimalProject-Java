package com.example.enclosure.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Service;

import com.example.enclosure.config.ProcessorGroups;
import com.example.enclosure.entity.Enclosure;
import com.example.enclosure.event.EnclosureUpdatedEvent;
import com.example.enclosure.repository.EnclosureRepository;
import com.example.enclosure.service.LoggingMQService;

@Service
@ProcessingGroup(ProcessorGroups.ENCLOSURE_UPDATED)
public class EnclosureUpdatedEventHandler {

    private final EnclosureRepository enclosureRepository;
    private final LoggingMQService loggingMQService;

    public EnclosureUpdatedEventHandler(EnclosureRepository enclosureRepository, LoggingMQService loggingMQService) {
        this.enclosureRepository = enclosureRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(EnclosureUpdatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling EnclosureUpdatedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling EnclosureUpdatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ENCLOSURE_UPDATED);

        Enclosure enclosure = enclosureRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalArgumentException("Enclosure not found"));
        enclosure.setName(event.getName());
        enclosure.setType(event.getType());
        enclosure.setLocation(event.getLocation());
        enclosure.setCapacity(event.getCapacity());
        enclosure.setCurrentAnimalCount(event.getCurrentAnimalCount());
        enclosureRepository.save(enclosure);

        long duration = System.currentTimeMillis() - startTime;
        loggingMQService.logEmWithDuration(
                "Enclosure updated",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ENCLOSURE_UPDATED,
                duration);
    }
}