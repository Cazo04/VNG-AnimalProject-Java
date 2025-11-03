package com.example.logging.handler;

import org.springframework.stereotype.Component;
import org.axonframework.queryhandling.QueryHandler;

import com.example.logging.repository.ApiLoggingRepository;
import com.example.logging.query.GetAllApiLogsQuery;
import com.example.logging.entity.ApiLogging;

import java.util.List;

@Component
public class ApiLoggingQueryHandler {
    
    private final ApiLoggingRepository apiLoggingRepository;

    public ApiLoggingQueryHandler(ApiLoggingRepository apiLoggingRepository) {
        this.apiLoggingRepository = apiLoggingRepository;
    }

    @QueryHandler
    public List<ApiLogging> handle(GetAllApiLogsQuery query) {
        return apiLoggingRepository.findAll();
    }
}
