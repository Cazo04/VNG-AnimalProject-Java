package com.example.feeding.handler;

import org.springframework.stereotype.Component;
import org.axonframework.queryhandling.QueryHandler;

import com.example.feeding.repository.FeedingRepository;
import com.example.feeding.query.GetAllFeedingQuery;
import com.example.feeding.entity.Feeding;

import java.util.List;

@Component
public class FeedingQueryHandler {

    private final FeedingRepository feedingRepository;

    public FeedingQueryHandler(FeedingRepository feedingRepository) {
        this.feedingRepository = feedingRepository;
    }

    @QueryHandler
    public List<Feeding> handle(GetAllFeedingQuery query) {
        return feedingRepository.findAll();
    }
}