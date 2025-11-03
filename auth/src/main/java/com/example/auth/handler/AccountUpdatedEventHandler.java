package com.example.auth.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth.config.ProcessorGroups;
import com.example.auth.entity.Account;
import com.example.auth.entity.enums.Role;
import com.example.auth.event.AccountUpdatedEvent;
import com.example.auth.repository.AccountRepository;
import com.example.auth.service.LoggingMQService;

@Component
@ProcessingGroup(ProcessorGroups.ACCOUNT_UPDATED)
public class AccountUpdatedEventHandler {

    private final AccountRepository accountRepository;
    private final LoggingMQService loggingMQService;
    private final PasswordEncoder passwordEncoder;

    public AccountUpdatedEventHandler(AccountRepository accountRepository, 
                                      LoggingMQService loggingMQService,
                                      PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.loggingMQService = loggingMQService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventHandler
    public void on(AccountUpdatedEvent event, EventMessage<?> eventMessage) {
        System.out.println("Handling AccountUpdatedEvent: " + eventMessage.getIdentifier());

        long startTime = System.currentTimeMillis();
        loggingMQService.logEm(
                "Handling AccountUpdatedEvent",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ACCOUNT_UPDATED);

        Account account = accountRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setUsername(event.getUsername());
        account.setEmail(event.getEmail());
        if (event.getPassword() != null && !event.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(event.getPassword()));
        }
        account.setRole(Role.valueOf(event.getRole()));

        accountRepository.save(account);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Account updated with ID: " + account.getId());
        loggingMQService.logEmWithDuration(
                "Account updated",
                eventMessage.getIdentifier(),
                event.getRequestId(),
                ProcessorGroups.ACCOUNT_UPDATED,
                duration);
    }
}
