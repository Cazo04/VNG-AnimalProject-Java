package com.example.logging.handler;

import org.springframework.stereotype.Component;
import org.axonframework.queryhandling.QueryHandler;

import com.example.logging.repository.SysLoggingRepository;
import com.example.logging.query.GetAllSysLogsQuery;
import com.example.logging.entity.SysLogging;

import java.util.List;

@Component
public class SysLoggingQueryHandler {
    
    private final SysLoggingRepository sysLoggingRepository;

    public SysLoggingQueryHandler(SysLoggingRepository sysLoggingRepository) {
        this.sysLoggingRepository = sysLoggingRepository;
    }

    @QueryHandler
    public List<SysLogging> handle(GetAllSysLogsQuery query) {
        return sysLoggingRepository.findAll();
    }
}
