package com.example.enclosure.config;

public final class ProcessorGroups {
    
    private ProcessorGroups() {
        // Prevent instantiation
    }

    public static final String ENCLOSURE_CREATED = "enclosure-created-processor-group";
    public static final String ENCLOSURE_UPDATED = "enclosure-updated-processor-group";
    public static final String ENCLOSURE_DELETED = "enclosure-deleted-processor-group";
    public static final String ENCLOSURE_UPDATED_ANIMAL_COUNT = "enclosure-updated-animal-count-processor-group";
}