package com.example.enclosure.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.enclosure.dto.AnimalCreatedEventDto;
import com.example.enclosure.entity.Enclosure;
import com.example.enclosure.repository.EnclosureRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AnimalEventListener {

    private final EnclosureRepository enclosureRepository;
    private final ObjectMapper objectMapper;

    public AnimalEventListener(EnclosureRepository enclosureRepository, ObjectMapper objectMapper) {
        this.enclosureRepository = enclosureRepository;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "animal.service.created")
    public void handleAnimalCreated(AnimalCreatedEventDto dto) {
        try {
            if (dto.enclosureId() != null) {
                Enclosure enclosure = enclosureRepository.findById(dto.enclosureId())
                    .orElseThrow(() -> new IllegalArgumentException("Enclosure not found: " + dto.enclosureId()));

                // Increment animal count
                Integer currentCount = enclosure.getCurrentAnimalCount() != null ? enclosure.getCurrentAnimalCount() : 0;
                enclosure.setCurrentAnimalCount(currentCount + 1);
                enclosureRepository.save(enclosure);

                System.out.println("Updated enclosure " + dto.enclosureId() + " animal count to " + enclosure.getCurrentAnimalCount());
            }
        } catch (Exception e) {
            System.err.println("Error handling animal created event: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
