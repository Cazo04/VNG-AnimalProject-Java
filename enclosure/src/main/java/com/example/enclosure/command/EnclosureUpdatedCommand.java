package com.example.enclosure.command;

public class EnclosureUpdatedCommand {
    private Long id;
    private String name;
    private String type;
    private String location;
    private Integer capacity;

    public EnclosureUpdatedCommand(Long id, String name, String type, String location, Integer capacity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public Integer getCapacity() { return capacity; }
}
