
package com.example.health.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import com.example.health.command.HealthCreatedCommand;
import com.example.health.command.HealthUpdatedCommand;
import com.example.health.command.HealthDeletedCommand;
import com.example.health.dto.HealthCreatedRequestDto;
import com.example.health.dto.HealthUpdateRequestDto;
import com.example.health.query.GetAllHealthQuery;
import com.example.health.entity.Health;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public HealthController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public ResponseEntity<?> createHealth(@RequestBody HealthCreatedRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        HealthCreatedCommand command = new HealthCreatedCommand(
                requestId,
                requestDto.getAnimalId(),
                requestDto.getStaffId(),
                requestDto.getWeight(),
                requestDto.getStatus(),
                requestDto.getActivity(),
                requestDto.getCheckTime());

        commandGateway.send(command);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Health creation initiated");

        return ResponseEntity.accepted()
                .header("X-Request-Id", requestId)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<Health>> getAllHealth() {
        List<Health> healthList = queryGateway.query(new GetAllHealthQuery(), ResponseTypes.multipleInstancesOf(Health.class))
                .join();
        return ResponseEntity.ok(healthList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHealth(@PathVariable Long id, @RequestBody HealthUpdateRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        HealthUpdatedCommand command = new HealthUpdatedCommand(
                requestId,
                id,
                requestDto.getAnimalId(),
                requestDto.getStaffId(),
                requestDto.getWeight(),
                requestDto.getStatus(),
                requestDto.getActivity(),
                requestDto.getCheckTime());
        commandGateway.sendAndWait(command);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Health update initiated");
        response.put("status", "SUCCESS");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHealth(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        HealthDeletedCommand command = new HealthDeletedCommand(requestId, id);
        commandGateway.sendAndWait(command);
        return ResponseEntity.noContent().build();
    }
}
