package com.example.enclosure.handler;

import org.springframework.stereotype.Component;
import org.axonframework.queryhandling.QueryHandler;

import com.example.enclosure.repository.EnclosureRepository;
import com.example.enclosure.query.GetAllEnclosureQuery;
import com.example.enclosure.entity.Enclosure;

import java.util.List;

@Component
public class EnclosureQueryHandler {
    
    private final EnclosureRepository enclosureRepository;

    public EnclosureQueryHandler(EnclosureRepository enclosureRepository) {
        this.enclosureRepository = enclosureRepository;
    }

    @QueryHandler
    public List<Enclosure> handle(GetAllEnclosureQuery query) {
        return enclosureRepository.findAll();
    }
}
