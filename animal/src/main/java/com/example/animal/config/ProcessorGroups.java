package com.example.animal.config;

public final class ProcessorGroups {
    
    private ProcessorGroups() {
        // Prevent instantiation
    }

    public static final String ANIMAL_CREATED = "animal-created-processor-group";
    public static final String ANIMAL_UPDATED = "animal-updated-processor-group";
    public static final String ANIMAL_DELETED = "animal-deleted-processor-group";
}