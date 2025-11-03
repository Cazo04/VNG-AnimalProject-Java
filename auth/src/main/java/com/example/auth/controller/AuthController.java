package com.example.auth.controller;

import org.springframework.web.bind.annotation.RestController;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.auth.command.AccountCreatedCommand;
import com.example.auth.command.AccountUpdatedCommand;
import com.example.auth.command.AccountDeletedCommand;
import com.example.auth.dto.AccountCreatedRequestDto;
import com.example.auth.dto.AccountUpdateRequestDto;
import com.example.auth.dto.LoginRequestDto;
import com.example.auth.dto.LoginResponseDto;
import com.example.auth.query.GetAllAccountsQuery;
import com.example.auth.entity.Account;
import com.example.auth.util.JwtUtil;
import com.example.auth.service.AuthService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public AuthController(CommandGateway commandGateway, 
                         QueryGateway queryGateway,
                         AuthenticationManager authenticationManager,
                         JwtUtil jwtUtil,
                         AuthService authService) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.username(), requestDto.password())
            );

            Account account = authService.findByUsername(requestDto.username());
            String token = jwtUtil.generateToken(requestDto.username(), account.getRole().name());

            LoginResponseDto response = new LoginResponseDto(
                token,
                requestDto.username(),
                account.getRole().name()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(error);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@org.springframework.web.bind.annotation.RequestHeader("Authorization") String authHeader) {
        try {
            // Check if Authorization header is present and starts with "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Missing or invalid Authorization header");
                return ResponseEntity.status(401).body(error);
            }

            // Extract token from "Bearer <token>"
            String token = authHeader.substring(7);

            // Extract username from token
            String username = jwtUtil.extractUsername(token);

            // Validate token
            if (!jwtUtil.validateToken(token, username)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid or expired token");
                return ResponseEntity.status(401).body(error);
            }

            // Get account details
            Account account = authService.findByUsername(username);

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("username", account.getUsername());
            response.put("role", account.getRole().name());
            response.put("email", account.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token verification failed: " + e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountCreatedRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        AccountCreatedCommand command = new AccountCreatedCommand(
                requestId,
                requestDto.username(),
                requestDto.email(),
                requestDto.password(),
                requestDto.role());

        commandGateway.send(command);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Account registration initiated");

        return ResponseEntity.accepted()
                .header("X-Request-Id", requestId)
                .body(response);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = queryGateway.query(new GetAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class))
                .join();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody AccountUpdateRequestDto requestDto) {
        String requestId = UUID.randomUUID().toString();
        AccountUpdatedCommand command = new AccountUpdatedCommand(
                requestId,
                id,
                requestDto.username(),
                requestDto.email(),
                requestDto.password(),
                requestDto.role());
        commandGateway.sendAndWait(command);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Account update initiated");
        response.put("status", "SUCCESS");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        AccountDeletedCommand command = new AccountDeletedCommand(id);
        commandGateway.sendAndWait(command);
        return ResponseEntity.noContent().build();
    }
}
