package com.example.enclosure.handler;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.example.enclosure.entity.Enclosure;
import com.example.enclosure.event.EnclosureCreatedEvent;
import com.example.enclosure.event.EnclosureUpdatedEvent;
import com.example.enclosure.event.EnclosureDeletedEvent;
import com.example.enclosure.repository.EnclosureRepository;
import com.example.enclosure.service.MessagingService;

@Component
public class EnclosureEventHandler {
    
    private final EnclosureRepository enclosureRepository;
    private final MessagingService messagingService;

    public EnclosureEventHandler(EnclosureRepository enclosureRepository, MessagingService messagingService) {
        this.enclosureRepository = enclosureRepository;
        this.messagingService = messagingService;
    }

    @EventHandler
    public void on(EnclosureCreatedEvent event) {
        try {
            // Save to database
            Enclosure enclosure = new Enclosure(
                event.getName(),
                event.getType(),
                event.getLocation(),
                event.getCapacity(),
                0 // initial animal count
            );
            Enclosure savedEnclosure = enclosureRepository.save(enclosure);
            
            // Log success
            messagingService.logDb("Enclosure Created", 200,
                "Enclosure ID: " + savedEnclosure.getId() + ", Name: " + savedEnclosure.getName());
            
        } catch (Exception e) {
            // Log error
            messagingService.logDbError("Enclosure Creation Failed", e);
            throw new RuntimeException("Failed to save enclosure", e);
        }
    }

    @EventHandler
    public void on(EnclosureUpdatedEvent event) {
        try {
            Enclosure enclosure = enclosureRepository.findById(event.getEnclosureId())
                .orElseThrow(() -> new IllegalArgumentException("Enclosure not found: " + event.getEnclosureId()));
            enclosure.setName(event.getName());
            enclosure.setType(event.getType());
            enclosure.setLocation(event.getLocation());
            enclosure.setCapacity(event.getCapacity());
            Enclosure saved = enclosureRepository.save(enclosure);

            messagingService.logDb("Enclosure Updated", 200,
                "Enclosure ID: " + saved.getId() + ", Name: " + saved.getName());
        } catch (Exception e) {
            messagingService.logDbError("Enclosure Update Failed", e);
            throw new RuntimeException("Failed to update enclosure", e);
        }
    }

    @EventHandler
    public void on(EnclosureDeletedEvent event) {
        try {
            enclosureRepository.deleteById(event.getEnclosureId());
            messagingService.logDb("Enclosure Deleted", 200, "Enclosure ID: " + event.getEnclosureId());
        } catch (Exception e) {
            messagingService.logDbError("Enclosure Delete Failed", e);
            throw new RuntimeException("Failed to delete enclosure", e);
        }
    }
}
