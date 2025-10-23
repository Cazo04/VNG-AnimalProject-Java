package com.example.report.handler;

import com.example.report.dto.AnimalHealthCheckDto;
import com.example.report.dto.DailyFeedingDto;
import com.example.report.query.AnimalHealthCheckQuery;
import com.example.report.query.DailyFeedingQuery;
import com.example.report.repository.interfaces.FeedingRepository;
import com.example.report.repository.interfaces.HealthRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ReportQueryHandler {
    
    private final FeedingRepository feedingRepository;
    private final HealthRepository healthRepository;

    public ReportQueryHandler(FeedingRepository feedingRepository, HealthRepository healthRepository) {
        this.feedingRepository = feedingRepository;
        this.healthRepository = healthRepository;
    }

    @QueryHandler
    public List<DailyFeedingDto> handle(DailyFeedingQuery query) {
        LocalDate targetDate = query.getDate() != null 
            ? query.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            : null;
        
        return feedingRepository.findDailyFeeding(targetDate);
    }

    @QueryHandler
    public List<AnimalHealthCheckDto> handle(AnimalHealthCheckQuery query) {
        LocalDateTime fromDateTime = query.getFromDate() != null
            ? query.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            : null;
        
        LocalDateTime toDateTime = query.getToDate() != null
            ? query.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            : null;

        return healthRepository.findHealthChecks(fromDateTime, toDateTime);
    }
}
