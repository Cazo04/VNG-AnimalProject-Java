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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.animal.command.AnimalCreatedCommand;
import com.example.animal.command.AnimalUpdatedCommand;
import com.example.animal.command.AnimalDeletedCommand;
import com.example.animal.dto.AnimalCreatedRequestDto;
import com.example.animal.dto.AnimalUpdateRequestDto;
import com.example.animal.query.GetAllAnimalQuery;
import com.example.animal.entity.Animal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
    String requestId = UUID.randomUUID().toString();
    AnimalCreatedCommand command = new AnimalCreatedCommand(
        requestId,
        requestDto.getName(),
        requestDto.getSpecies(),
        requestDto.getGender(),
        requestDto.getAge(),
        LocalDateTime.now(),
        requestDto.getEnclosureId()
    );
    commandGateway.send(command);
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Animal creation initiated");
    response.put("status", "SUCCESS");
    return ResponseEntity.accepted().body(response);
    }

    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        List<Animal> animals = queryGateway.query(new GetAllAnimalQuery(), ResponseTypes.multipleInstancesOf(Animal.class))
                .join();
    return ResponseEntity.ok().body(animals);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnimal(@PathVariable Long id, @RequestBody AnimalUpdateRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        AnimalUpdatedCommand command = new AnimalUpdatedCommand(
                requestId,
                id,
                requestDto.getName(),
                requestDto.getSpecies(),
                requestDto.getGender(),
                requestDto.getAge(),
                requestDto.getArrivalDate(),
                requestDto.getEnclosureId()
        );
        commandGateway.send(command);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Animal update initiated");
        response.put("status", "SUCCESS");
    return ResponseEntity.accepted().body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        AnimalDeletedCommand command = new AnimalDeletedCommand(requestId, id);
        commandGateway.send(command);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Animal deletion initiated");
        response.put("status", "SUCCESS");
    return ResponseEntity.accepted().body(response);
    }
}