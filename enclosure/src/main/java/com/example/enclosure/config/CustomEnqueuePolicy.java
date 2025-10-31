package com.example.enclosure.config;

import java.util.concurrent.ConcurrentMap;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.deadletter.DeadLetter;
import org.axonframework.messaging.deadletter.Decisions;
import org.axonframework.messaging.deadletter.EnqueueDecision;
import org.axonframework.messaging.deadletter.EnqueuePolicy;
import com.example.enclosure.service.LoggingMQService;

public class CustomEnqueuePolicy implements EnqueuePolicy<EventMessage<?>> {
    private static final String RETRIES_KEY = "retries";
    private final ConcurrentMap<String, Integer> retryCounts = new java.util.concurrent.ConcurrentHashMap<>();
    private final LoggingMQService loggingMQService;

    public CustomEnqueuePolicy(LoggingMQService loggingMQService) {
        this.loggingMQService = loggingMQService;
    }

    @Override
    public EnqueueDecision<EventMessage<?>> decide(DeadLetter<? extends EventMessage<?>> letter, Throwable cause) {
        String eventId = letter.message().getIdentifier();
        int retries = retryCounts.getOrDefault(eventId, 0);
        if (retries < 3) {
            retryCounts.put(eventId, ++retries);
            System.out.printf("Retrying event %s, attempt %d%n", eventId, retries);
            loggingMQService.logEmError("Retrying event, attempt " + retries, eventId, (Exception) cause);
            throw new RuntimeException(cause);
        }
        retryCounts.remove(eventId);
        System.out.printf("Max retries reached for event %s. Enqueuing to DLQ.%n", eventId);
        loggingMQService.logEmError("Max retries reached for event. Enqueuing to DLQ.", eventId, (Exception) cause);
        return Decisions.enqueue(cause, l -> MetaData.with(RETRIES_KEY, 0));
    }
}