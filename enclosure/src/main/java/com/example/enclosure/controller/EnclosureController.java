package com.example.enclosure.controller;

import com.example.enclosure.command.EnclosureCreatedCommand;
import com.example.enclosure.command.EnclosureDeletedCommand;
import com.example.enclosure.command.EnclosureUpdatedCommand;
import com.example.enclosure.dto.EnclosureCreatedRequestDto;
import com.example.enclosure.dto.EnclosureUpdateRequestDto;
import com.example.enclosure.entity.Enclosure;
import com.example.enclosure.query.GetAllEnclosuresQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<String> createEnclosure(@RequestBody EnclosureCreatedRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        EnclosureCreatedCommand command = new EnclosureCreatedCommand(
                requestId,
                requestDto.name(),
                requestDto.type(),
                requestDto.location(),
                requestDto.capacity(),
                0
        );
        commandGateway.sendAndWait(command);
        return ResponseEntity.ok("Enclosure created with request ID: " + requestId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEnclosure(@PathVariable Long id, @RequestBody EnclosureUpdateRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        EnclosureUpdatedCommand command = new EnclosureUpdatedCommand(
                requestId,
                id,
                requestDto.name(),
                requestDto.type(),
                requestDto.location(),
                requestDto.capacity(),
                0
        );
        commandGateway.sendAndWait(command);
        return ResponseEntity.ok("Enclosure updated with request ID: " + requestId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEnclosure(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        EnclosureDeletedCommand command = new EnclosureDeletedCommand(requestId, id);
        commandGateway.sendAndWait(command);
        return ResponseEntity.ok("Enclosure deleted with request ID: " + requestId);
    }

    @GetMapping
    public ResponseEntity<List<Enclosure>> getAllEnclosures() {
        List<Enclosure> enclosures = queryGateway.query(
                new GetAllEnclosuresQuery(),
                ResponseTypes.multipleInstancesOf(Enclosure.class)
        ).join();
        return ResponseEntity.ok(enclosures);
    }
}