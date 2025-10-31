package com.example.feeding.handler;

import org.springframework.stereotype.Component;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;

import com.example.feeding.config.ProcessorGroups;
import com.example.feeding.entity.Feeding;
import com.example.feeding.event.FeedingUpdatedEvent;
import com.example.feeding.repository.FeedingRepository;
import com.example.feeding.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.FEEDING_UPDATED)
public class FeedingUpdatedEventHandler {

    private final FeedingRepository feedingRepository;
    private final LoggingMQService loggingMQService;

    public FeedingUpdatedEventHandler(FeedingRepository feedingRepository, LoggingMQService loggingMQService) {
        this.feedingRepository = feedingRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(FeedingUpdatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling FeedingUpdatedEvent: " + eventMessage.getIdentifier());
        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling FeedingUpdatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.FEEDING_UPDATED);

        Feeding feeding = feedingRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalArgumentException("Feeding not found: " + event.getId()));
        feeding.setAnimalId(event.getAnimalId());
        feeding.setFoodType(event.getFoodType());
        feeding.setQuantity(event.getQuantity());
        feeding.setFeedingTime(event.getFeedingTime());
        feeding.setKeeperId(event.getKeeperId());
        Feeding saved = feedingRepository.save(feeding);

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Feeding updated with ID: " + saved.getId());
        loggingMQService.logEmWithDuration(
                "Feeding updated",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.FEEDING_UPDATED,
                duration);
    }
}