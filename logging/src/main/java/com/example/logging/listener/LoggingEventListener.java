package com.example.logging.listener;

import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;
import com.example.logging.dto.ApiLogDto;
import com.example.logging.dto.SysLogDto;
import com.example.logging.entity.ApiLogging;
import com.example.logging.entity.SysLogging;
import com.example.logging.repository.ApiLoggingRepository;
import com.example.logging.repository.SysLoggingRepository;
import com.example.logging.mapper.ApiLoggingMapper;
import com.example.logging.mapper.SysLoggingMapper;

@Component
public class LoggingEventListener {

    private static final String ZOO_API_SERVICE_LOGGING_TOPIC = "zoo.api.service.logs";
    private static final String ZOO_SYS_SERVICE_LOGGING_TOPIC = "zoo.sys.service.logs";
    private final ApiLoggingRepository apiLoggingRepository;
    private final SysLoggingRepository sysLoggingRepository;
    private final ApiLoggingMapper apiLoggingMapper;
    private final SysLoggingMapper sysLoggingMapper;

    public LoggingEventListener(
            ApiLoggingRepository apiLoggingRepository,
            SysLoggingRepository sysLoggingRepository,
            ApiLoggingMapper apiLoggingMapper,
            SysLoggingMapper sysLoggingMapper) {
        this.apiLoggingRepository = apiLoggingRepository;
        this.sysLoggingRepository = sysLoggingRepository;
        this.apiLoggingMapper = apiLoggingMapper;
        this.sysLoggingMapper = sysLoggingMapper;
    }

    @JmsListener(destination = ZOO_API_SERVICE_LOGGING_TOPIC)
    public void onApiLoggingEvent(ApiLogDto logDto) {
        System.out.println("Received API logging event: " + logDto);
        ApiLogging entity = apiLoggingMapper.toEntity(logDto);
        apiLoggingRepository.save(entity);
    }

    @JmsListener(destination = ZOO_SYS_SERVICE_LOGGING_TOPIC)
    public void onSysLoggingEvent(SysLogDto sysLogDto) {
        System.out.println("Received SYS logging event: " + sysLogDto);
        SysLogging entity = sysLoggingMapper.toEntity(sysLogDto);
        
        sysLoggingRepository.save(entity);
    }
}
