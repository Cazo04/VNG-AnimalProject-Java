package com.example.enclosure.event;

public class EnclosureCreatedEvent {
    private String name;
    private String type;
    private String location;
    private Integer capacity;
    private Long enclosureId; // Database ID after persistence

    public EnclosureCreatedEvent(String name, String type, String location, Integer capacity) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Long getEnclosureId() {
        return enclosureId;
    }

    public void setEnclosureId(Long enclosureId) {
        this.enclosureId = enclosureId;
    }
}
