

package com.example.health.handler;

import com.example.health.config.ProcessorGroups;
import com.example.health.entity.Health;
import com.example.health.event.HealthCreatedEvent;
import com.example.health.repository.HealthRepository;
import com.example.health.service.LoggingMQService;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup(ProcessorGroups.HEALTH_CREATED)
public class HealthCreatedEventHandler {
    private final HealthRepository healthRepository;
    private final LoggingMQService loggingMQService;

    public HealthCreatedEventHandler(HealthRepository healthRepository, LoggingMQService loggingMQService) {
        this.healthRepository = healthRepository;
        this.loggingMQService = loggingMQService;
    }

    @EventHandler
    public void on(HealthCreatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling HealthCreatedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling HealthCreatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.HEALTH_CREATED);

        Health health = new Health(
                null,
                event.getAnimalId(),
                event.getStaffId(),
                event.getWeight(),
                event.getStatus(),
                event.getActivity(),
                event.getCheckTime()
        );
        Health savedHealth = healthRepository.save(health);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Health created with ID: " + savedHealth.getId());
        loggingMQService.logEmWithDuration(
                "Health created",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.HEALTH_CREATED,
                duration);
    }
}
