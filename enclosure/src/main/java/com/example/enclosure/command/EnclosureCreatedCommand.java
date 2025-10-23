package com.example.enclosure.command;

public class EnclosureCreatedCommand {
    private String name;
    private String type;
    private String location;
    private Integer capacity;

    public EnclosureCreatedCommand(String name, String type, String location, Integer capacity) {
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
}
