package com.example.enclosure.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Service;

import com.example.enclosure.config.ProcessorGroups;
import com.example.enclosure.event.EnclosureDeletedEvent;
import com.example.enclosure.repository.EnclosureRepository;
import com.example.enclosure.service.LoggingMQService;

@Service
@ProcessingGroup(ProcessorGroups.ENCLOSURE_DELETED)
public class EnclosureDeletedEventHandler {

    private final EnclosureRepository enclosureRepository;
    private final LoggingMQService loggingMQService;

    public EnclosureDeletedEventHandler(EnclosureRepository enclosureRepository, LoggingMQService loggingMQService) {
        this.enclosureRepository = enclosureRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(EnclosureDeletedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling EnclosureDeletedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling EnclosureDeletedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ENCLOSURE_DELETED);

        enclosureRepository.deleteById(event.getId());

        long duration = System.currentTimeMillis() - startTime;
        loggingMQService.logEmWithDuration(
                "Enclosure deleted",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ENCLOSURE_DELETED,
                duration);
    }
}