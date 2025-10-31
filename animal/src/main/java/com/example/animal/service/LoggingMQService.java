package com.example.animal.service;

import com.example.animal.dto.ApiLogDto;
import com.example.animal.dto.SysLogDto;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.animal.dto.AnimalCreatedMQDto;

@Service
public class LoggingMQService {
    private static final String SERVICE_NAME = "animal";
    private static final String ZOO_API_SERVICE_LOGGING_TOPIC = "zoo.api.service.logs";
    private static final String ZOO_SYS_SERVICE_LOGGING_TOPIC = "zoo.sys.service.logs";
    private static final String API_LOG_TYPE = "ApiLoggingEvent";
    private static final String SYS_LOG_TYPE = "SysLoggingEvent";
    private static final String ANIMAL_CREATED_EVENT_TOPIC = "animal.created.events";
    private static final String ANIMAL_CREATED_EVENT_TYPE = "AnimalCreatedEvent";
    private final JmsTemplate jmsTemplate;

    public LoggingMQService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendAnimalCreatedEvent(Long animalId, Long enclosureId) {
        var payload = new AnimalCreatedMQDto(animalId, enclosureId);
        MessagePostProcessor msg = message -> {
            message.setStringProperty("_type", ANIMAL_CREATED_EVENT_TYPE);
            return message;
        };
        send(ANIMAL_CREATED_EVENT_TOPIC, payload, msg);
    }

    public void sendApiLog(ApiLogDto logDto) {
        MessagePostProcessor msg = message -> {
            message.setStringProperty("_type", API_LOG_TYPE);
            return message;
        };
        send(ZOO_API_SERVICE_LOGGING_TOPIC, logDto, msg);
    }

    public void sendSysLog(SysLogDto logDto) {
        MessagePostProcessor msg = message -> {
            message.setStringProperty("_type", SYS_LOG_TYPE);
            return message;
        };
        send(ZOO_SYS_SERVICE_LOGGING_TOPIC, logDto, msg);
    }

    public void logEmWithDuration(String action, String eventId, String requestId, String processingGroup, long duration) {
        SysLogDto log = new SysLogDto();
        log.setServiceName(SERVICE_NAME);
        log.setRequestId(UUID.fromString(requestId));
        log.setEventId(eventId);
        log.setProcessingGroup(processingGroup);
        log.setDuration(duration);
        log.setTimestamp(LocalDateTime.now());
        log.setAction(action);

        sendSysLog(log);
    }

    public void logEm(String action, String eventId, String requestId, String processingGroup) {
        logEmWithDuration(action, eventId, requestId, processingGroup, 0L);
    }

    public void logEmError(String action, String eventId, Exception e) {
        SysLogDto log = new SysLogDto();
        log.setServiceName(SERVICE_NAME);
        log.setEventId(eventId);
        log.setTimestamp(LocalDateTime.now());
        log.setAction(action);
        log.setDuration(0L);
        log.setError(e.getMessage());

        sendSysLog(log);
    }

    public void send(String destination, Object payload, MessagePostProcessor msgProcessor) {
        try {
            jmsTemplate.convertAndSend(destination, payload, msgProcessor);
            System.out.println("Sent log message: " + payload);
        } catch (Exception ignored) {
            // ...existing code...
        }
    }
}