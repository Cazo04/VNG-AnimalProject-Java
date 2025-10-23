package com.example.enclosure.service;

import com.example.enclosure.dto.ApiLogDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessagingService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public MessagingService(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    // Topics
    public void sendZooLog(ApiLogDto logDto) {
        send("zoo.service.logs", logDto);
    }

    // Convenience logging helpers (minimize call sites)
    public void logDb(String action, int status, String details) {
        ApiLogDto log = new ApiLogDto(
                "enclosure.service", "DATABASE", action, status, 0L, LocalDateTime.now(), details);
        sendZooLog(log);
    }

    public void logDbError(String action, Exception e) {
        logDb(action, 500, "Error: " + (e != null ? e.getMessage() : "unknown"));
    }

    // Low-level sender
    public void send(String destination, Object payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            System.out.println("Sending message to " + destination + ": " + message);
            jmsTemplate.convertAndSend(destination, message);
        } catch (Exception ignored) {
            // swallow send errors to avoid breaking business flow
        }
    }
}
