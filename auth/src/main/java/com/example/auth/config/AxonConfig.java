package com.example.auth.config;

import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.messaging.deadletter.InMemorySequencedDeadLetterQueue;
import org.axonframework.config.ConfigurerModule;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.auth.service.LoggingMQService;

import java.util.Set;

@Configuration
public class AxonConfig {

    @Bean
    public EventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public EventStore eventStore(EventStorageEngine eventStorageEngine) {
        return EmbeddedEventStore.builder()
                .storageEngine(eventStorageEngine)
                .build();
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public ConfigurerModule deadLetterQueueAndPolicyModule(
        LoggingMQService loggingMQService
    ) {
        Set<String> processingGroups = Set.of(
                ProcessorGroups.ACCOUNT_CREATED,
                ProcessorGroups.ACCOUNT_UPDATED);
        
        return configurer -> configurer.eventProcessing(processingConfigurer -> {
            processingGroups.forEach(processingGroup -> {
                processingConfigurer.registerDeadLetterQueue(
                        processingGroup,
                        config -> InMemorySequencedDeadLetterQueue.<EventMessage<?>>builder().build())
                    .registerDeadLetterPolicy(
                        processingGroup,
                        config -> new CustomEnqueuePolicy(loggingMQService));
            });
        });
    }
}
