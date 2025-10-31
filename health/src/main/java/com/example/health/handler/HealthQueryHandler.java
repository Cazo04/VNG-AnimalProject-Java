package com.example.health.handler;

import com.example.health.entity.Health;
import com.example.health.query.GetAllHealthQuery;
import com.example.health.repository.HealthRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HealthQueryHandler {
    private final HealthRepository healthRepository;

    @Autowired
    public HealthQueryHandler(HealthRepository healthRepository) {
        this.healthRepository = healthRepository;
    }

    @QueryHandler
    public List<Health> handle(GetAllHealthQuery query) {
        return healthRepository.findAll();
    }
}
