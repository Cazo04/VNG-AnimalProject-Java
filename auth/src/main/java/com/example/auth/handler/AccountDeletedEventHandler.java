package com.example.auth.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.example.auth.config.ProcessorGroups;
import com.example.auth.event.AccountDeletedEvent;
import com.example.auth.repository.AccountRepository;

@Component
@ProcessingGroup(ProcessorGroups.ACCOUNT_DELETED)
public class AccountDeletedEventHandler {

    private final AccountRepository accountRepository;

    public AccountDeletedEventHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        System.out.println("Handling AccountDeletedEvent for ID: " + event.getId());
        accountRepository.deleteById(event.getId());
        System.out.println("Account deleted with ID: " + event.getId());
    }
}
