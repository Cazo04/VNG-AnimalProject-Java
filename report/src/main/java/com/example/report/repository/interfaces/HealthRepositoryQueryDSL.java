package com.example.report.repository.interfaces;

import com.example.report.dto.AnimalHealthCheckDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthRepositoryQueryDSL {
    List<AnimalHealthCheckDto> findHealthChecks(LocalDateTime fromDateTime, LocalDateTime toDateTime);
}
