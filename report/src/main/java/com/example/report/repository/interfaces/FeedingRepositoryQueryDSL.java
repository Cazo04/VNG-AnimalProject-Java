package com.example.report.repository.interfaces;

import com.example.report.dto.DailyFeedingDto;

import java.time.LocalDate;
import java.util.List;

public interface FeedingRepositoryQueryDSL {
    List<DailyFeedingDto> findDailyFeeding(LocalDate targetDate);
}
