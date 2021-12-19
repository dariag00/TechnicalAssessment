package com.bsd.technicalassessment.account.service.service;

import com.bsd.technicalassessment.account.service.exception.AccountAlreadyExistsException;
import com.bsd.technicalassessment.account.service.exception.AccountNotFoundException;
import com.bsd.technicalassessment.account.service.repository.AccountRepository;
import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.AccountCreationRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        log.info("Getting all the accounts");
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        log.info("Getting the account with id {}", id);
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Account findByIBAN(String iban) {
        log.info("Getting the account with iban {}", iban);
        return accountRepository.findByIban(iban).orElseThrow(() -> new AccountNotFoundException(iban));
    }

    @Override
    @Transactional
    public Account createAccount(AccountCreationRequest accountCreationRequest) {
        log.info("Creating an Account with iban {} and initial balance {}", accountCreationRequest.getIban(), accountCreationRequest.getInitialBalance());
        if(accountRepository.findByIban(accountCreationRequest.getIban()).isPresent()) {
            throw new AccountAlreadyExistsException(accountCreationRequest.getIban());
        }
        Account account = new Account();
        account.setIban(accountCreationRequest.getIban());
        if(accountCreationRequest.getInitialBalance() != null) {
            account.setBalance(accountCreationRequest.getInitialBalance());
        }
        return this.save(account);
    }

    @Override
    public Account save(Account account) {
        log.info("Persisting account {}", account);
        return accountRepository.save(account);
    }
}
