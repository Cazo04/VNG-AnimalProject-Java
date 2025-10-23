package com.example.animal.controller;

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
import com.example.animal.command.AnimalCreatedCommand;
import com.example.animal.command.AnimalUpdatedCommand;
import com.example.animal.command.AnimalDeletedCommand;
import com.example.animal.dto.AnimalCreatedRequestDto;
import com.example.animal.dto.AnimalUpdateRequestDto;
import com.example.animal.query.GetAllAnimalQuery;
import com.example.animal.entity.Animal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    
    public AnimalController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public ResponseEntity<?> createAnimal(@RequestBody AnimalCreatedRequestDto requestDto) {
        AnimalCreatedCommand command = new AnimalCreatedCommand(
                requestDto.name(),
                requestDto.species(),
                requestDto.gender(),
                requestDto.age(),
                requestDto.arrivalDate(),
                requestDto.enclosureId());

        // Send command and wait for result
        String aggregateId = commandGateway.sendAndWait(command);

        Map<String, Object> response = new HashMap<>();
        response.put("aggregateId", aggregateId);
        response.put("message", "Animal creation initiated");
        response.put("status", "SUCCESS");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        List<Animal> animals = queryGateway.query(new GetAllAnimalQuery(), ResponseTypes.multipleInstancesOf(Animal.class)).join();
        return ResponseEntity.ok(animals);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnimal(@PathVariable Long id, @RequestBody AnimalUpdateRequestDto requestDto) {
        AnimalUpdatedCommand command = new AnimalUpdatedCommand(
            id,
            requestDto.name(),
            requestDto.species(),
            requestDto.gender(),
            requestDto.age(),
            requestDto.arrivalDate(),
            requestDto.enclosureId()
        );
        commandGateway.sendAndWait(command);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Animal update initiated");
        response.put("status", "SUCCESS");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long id) {
        AnimalDeletedCommand command = new AnimalDeletedCommand(id);
        commandGateway.sendAndWait(command);
        return ResponseEntity.noContent().build();
    }

}
