package com.example.auth.handler;

import org.springframework.stereotype.Component;
import org.axonframework.queryhandling.QueryHandler;

import com.example.auth.repository.AccountRepository;
import com.example.auth.query.GetAllAccountsQuery;
import com.example.auth.query.GetAccountByUsernameQuery;
import com.example.auth.entity.Account;

import java.util.List;
import java.util.Optional;

@Component
public class AccountQueryHandler {
    
    private final AccountRepository accountRepository;

    public AccountQueryHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @QueryHandler
    public List<Account> handle(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public Optional<Account> handle(GetAccountByUsernameQuery query) {
        return accountRepository.findByUsername(query.getUsername());
    }
}
