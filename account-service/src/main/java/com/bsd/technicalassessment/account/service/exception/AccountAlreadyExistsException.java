package com.bsd.technicalassessment.account.service.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String iban) {
        super("There is already a registered account with iban " + iban);
    }
}
