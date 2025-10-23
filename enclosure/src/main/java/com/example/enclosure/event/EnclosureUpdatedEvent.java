package com.example.enclosure.event;

public class EnclosureUpdatedEvent {
    private Long enclosureId;
    private String name;
    private String type;
    private String location;
    private Integer capacity;

    public EnclosureUpdatedEvent(Long enclosureId, String name, String type, String location, Integer capacity) {
        this.enclosureId = enclosureId;
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
    }

    public Long getEnclosureId() { return enclosureId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public Integer getCapacity() { return capacity; }
}
