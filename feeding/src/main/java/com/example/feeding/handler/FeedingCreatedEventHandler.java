package com.example.feeding.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;

import com.example.feeding.config.ProcessorGroups;
import com.example.feeding.entity.Feeding;
import com.example.feeding.event.FeedingCreatedEvent;
import com.example.feeding.repository.FeedingRepository;
import com.example.feeding.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.FEEDING_CREATED)
public class FeedingCreatedEventHandler {

    private final FeedingRepository feedingRepository;
    private final LoggingMQService loggingMQService;

    public FeedingCreatedEventHandler(FeedingRepository feedingRepository, LoggingMQService loggingMQService) {
        this.feedingRepository = feedingRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(FeedingCreatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling FeedingCreatedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling FeedingCreatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.FEEDING_CREATED);

        Feeding feeding = new Feeding(
                null,
                event.getAnimalId(),
                event.getFoodType(),
                event.getQuantity(),
                event.getFeedingTime(),
                event.getKeeperId());
        Feeding savedFeeding = feedingRepository.save(feeding);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Feeding created with ID: " + savedFeeding.getId());
        loggingMQService.logEmWithDuration(
                "Feeding created",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.FEEDING_CREATED,
                duration);
    }
}