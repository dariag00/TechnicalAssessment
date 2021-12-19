package com.bsd.technicalassessment.account.service.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Account with id " + id + " does not exist");
    }

    public AccountNotFoundException(String iban) {
        super("Account with IBAN " + iban + " does not exist");
    }
}
