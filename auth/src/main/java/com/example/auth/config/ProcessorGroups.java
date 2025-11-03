package com.example.auth.config;

public final class ProcessorGroups {
    
    private ProcessorGroups() {
        // Prevent instantiation
    }

    public static final String ACCOUNT_CREATED = "account-created-processor-group";
    public static final String ACCOUNT_UPDATED = "account-updated-processor-group";
    public static final String ACCOUNT_DELETED = "account-deleted-processor-group";
}
