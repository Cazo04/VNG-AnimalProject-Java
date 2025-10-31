package com.example.feeding.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.feeding.command.FeedingCreatedCommand;
import com.example.feeding.command.FeedingUpdatedCommand;
import com.example.feeding.command.FeedingDeletedCommand;
import com.example.feeding.dto.FeedingCreatedRequestDto;
import com.example.feeding.dto.FeedingUpdateRequestDto;
import com.example.feeding.query.GetAllFeedingQuery;
import com.example.feeding.entity.Feeding;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/feeding")
public class FeedingController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public FeedingController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public ResponseEntity<?> createFeeding(@RequestBody FeedingCreatedRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        FeedingCreatedCommand command = new FeedingCreatedCommand(
                requestId,
                requestDto.getAnimalId(),
                requestDto.getFoodType(),
                requestDto.getQuantity(),
                requestDto.getFeedingTime(),
                requestDto.getKeeperId());

        // Send command and wait for result
        commandGateway.send(command);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Feeding creation initiated");

        return ResponseEntity.accepted()
                .header("X-Request-Id", requestId)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<Feeding>> getAllFeeding() {
        List<Feeding> feedings = queryGateway.query(new GetAllFeedingQuery(), ResponseTypes.multipleInstancesOf(Feeding.class))
                .join();
        return ResponseEntity.ok(feedings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeeding(@PathVariable Long id, @RequestBody FeedingUpdateRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        FeedingUpdatedCommand command = new FeedingUpdatedCommand(
                requestId,
                id,
                requestDto.getAnimalId(),
                requestDto.getFoodType(),
                requestDto.getQuantity(),
                requestDto.getFeedingTime(),
                requestDto.getKeeperId());
        commandGateway.sendAndWait(command);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Feeding update initiated");
        response.put("status", "SUCCESS");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeeding(@PathVariable Long id) {
        FeedingDeletedCommand command = new FeedingDeletedCommand(id);
        commandGateway.sendAndWait(command);
        return ResponseEntity.noContent().build();
    }
}
