package com.example.enclosure.handler;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import com.example.enclosure.entity.Enclosure;
import com.example.enclosure.query.GetAllEnclosuresQuery;
import com.example.enclosure.repository.EnclosureRepository;

import java.util.List;

@Service
public class EnclosureQueryHandler {

    private final EnclosureRepository enclosureRepository;

    public EnclosureQueryHandler(EnclosureRepository enclosureRepository) {
        this.enclosureRepository = enclosureRepository;
    }

    @QueryHandler
    public List<Enclosure> handle(GetAllEnclosuresQuery query) {
        return enclosureRepository.findAll();
    }
}