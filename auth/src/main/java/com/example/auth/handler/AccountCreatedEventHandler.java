package com.example.auth.handler;

import java.time.LocalDateTime;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth.config.ProcessorGroups;
import com.example.auth.entity.Account;
import com.example.auth.entity.enums.Role;
import com.example.auth.event.AccountCreatedEvent;
import com.example.auth.repository.AccountRepository;
import com.example.auth.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.ACCOUNT_CREATED)
public class AccountCreatedEventHandler {

    private final AccountRepository accountRepository;
    private final LoggingMQService loggingMQService;
    private final PasswordEncoder passwordEncoder;

    public AccountCreatedEventHandler(AccountRepository accountRepository, 
                                      LoggingMQService loggingMQService,
                                      PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.loggingMQService = loggingMQService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling AccountCreatedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling AccountCreatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ACCOUNT_CREATED);

        Account account = new Account(
                null,
                event.getUsername(),
                event.getEmail(),
                passwordEncoder.encode(event.getPassword()),
                Role.valueOf(event.getRole()),
                LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Account created with ID: " + savedAccount.getId());
        loggingMQService.logEmWithDuration(
                "Account created",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ACCOUNT_CREATED,
                duration);
    }
}
