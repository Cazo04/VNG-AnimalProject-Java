package com.example.logging.controller;

import com.example.logging.entity.ApiLogging;
import com.example.logging.entity.SysLogging;
import com.example.logging.query.GetAllApiLogsQuery;
import com.example.logging.query.GetAllSysLogsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LoggingController {

    private final QueryGateway queryGateway;

    public LoggingController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/api")
    public ResponseEntity<List<ApiLogging>> getAllApiLogs() {
        List<ApiLogging> logs = queryGateway.query(
            new GetAllApiLogsQuery(), 
            ResponseTypes.multipleInstancesOf(ApiLogging.class)
        ).join();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/sys")
    public ResponseEntity<List<SysLogging>> getAllSysLogs() {
        List<SysLogging> logs = queryGateway.query(
            new GetAllSysLogsQuery(), 
            ResponseTypes.multipleInstancesOf(SysLogging.class)
        ).join();
        return ResponseEntity.ok(logs);
    }
}
