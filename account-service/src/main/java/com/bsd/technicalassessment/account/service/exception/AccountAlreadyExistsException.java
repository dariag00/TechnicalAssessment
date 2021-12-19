package com.bsd.technicalassessment.account.service.exception;

import javax.validation.constraints.NotEmpty;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String iban) {
        super("There is already a registered account with iban " + iban);
    }
}
