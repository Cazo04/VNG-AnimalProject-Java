package com.example.enclosure.event;

public class EnclosureDeletedEvent {
    private Long enclosureId;

    public EnclosureDeletedEvent(Long enclosureId) {
        this.enclosureId = enclosureId;
    }

    public Long getEnclosureId() { return enclosureId; }
}
