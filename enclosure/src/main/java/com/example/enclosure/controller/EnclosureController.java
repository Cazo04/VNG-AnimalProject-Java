package com.example.enclosure.controller;

import org.springframework.web.bind.annotation.RestController;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.enclosure.command.EnclosureCreatedCommand;
import com.example.enclosure.command.EnclosureUpdatedCommand;
import com.example.enclosure.command.EnclosureDeletedCommand;
import com.example.enclosure.dto.EnclosureCreatedRequestDto;
import com.example.enclosure.dto.EnclosureUpdateRequestDto;
import com.example.enclosure.query.GetAllEnclosureQuery;
import com.example.enclosure.entity.Enclosure;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/enclosures")
public class EnclosureController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    
    public EnclosureController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public ResponseEntity<?> createEnclosure(@RequestBody EnclosureCreatedRequestDto requestDto) {
        EnclosureCreatedCommand command = new EnclosureCreatedCommand(
                requestDto.name(),
                requestDto.type(),
                requestDto.location(),
                requestDto.capacity());

        // Send command and wait for result
        String aggregateId = commandGateway.sendAndWait(command);

        Map<String, Object> response = new HashMap<>();
        response.put("aggregateId", aggregateId);
        response.put("message", "Enclosure creation initiated");
        response.put("status", "SUCCESS");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Enclosure>> getAllEnclosures() {
        List<Enclosure> enclosures = queryGateway.query(new GetAllEnclosureQuery(), ResponseTypes.multipleInstancesOf(Enclosure.class)).join();
        return ResponseEntity.ok(enclosures);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnclosure(@PathVariable Long id, @RequestBody EnclosureUpdateRequestDto requestDto) {
        EnclosureUpdatedCommand command = new EnclosureUpdatedCommand(
            id,
            requestDto.name(),
            requestDto.type(),
            requestDto.location(),
            requestDto.capacity()
        );
        commandGateway.sendAndWait(command);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Enclosure update initiated");
        response.put("status", "SUCCESS");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnclosure(@PathVariable Long id) {
        EnclosureDeletedCommand command = new EnclosureDeletedCommand(id);
        commandGateway.sendAndWait(command);
        return ResponseEntity.noContent().build();
    }

}
