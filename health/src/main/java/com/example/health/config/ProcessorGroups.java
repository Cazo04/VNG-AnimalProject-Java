package com.example.health.config;

public final class ProcessorGroups {
    
    private ProcessorGroups() {
        // Prevent instantiation
    }

    public static final String HEALTH_CREATED = "health-created-processor-group";
    public static final String HEALTH_UPDATED = "health-updated-processor-group";
    public static final String HEALTH_DELETED = "health-deleted-processor-group";
}
