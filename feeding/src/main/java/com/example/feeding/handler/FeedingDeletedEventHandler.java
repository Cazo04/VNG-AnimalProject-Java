package com.example.feeding.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Service;
import com.example.feeding.config.ProcessorGroups;
import com.example.feeding.event.FeedingDeletedEvent;
import com.example.feeding.repository.FeedingRepository;

@Service
@ProcessingGroup(ProcessorGroups.FEEDING_DELETED)
public class FeedingDeletedEventHandler {

    private final FeedingRepository feedingRepository;

    public FeedingDeletedEventHandler(FeedingRepository feedingRepository) {
        this.feedingRepository = feedingRepository;
    }

    @EventHandler
    public void on(FeedingDeletedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling FeedingDeletedEvent: " + eventMessage.getIdentifier());
        feedingRepository.deleteById(event.getId());

        System.out.println("Feeding deleted with ID: " + event.getId());
    }
}