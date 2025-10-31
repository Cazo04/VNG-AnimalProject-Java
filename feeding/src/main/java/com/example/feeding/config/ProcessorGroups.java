package com.example.feeding.config;

public final class ProcessorGroups {
    
    private ProcessorGroups() {
        // Prevent instantiation
    }

    public static final String FEEDING_CREATED = "feeding-created-processor-group";
    public static final String FEEDING_UPDATED = "feeding-updated-processor-group";
    public static final String FEEDING_DELETED = "feeding-deleted-processor-group";
}
