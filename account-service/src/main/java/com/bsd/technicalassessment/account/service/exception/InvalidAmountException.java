package com.bsd.technicalassessment.account.service.exception;

import java.math.BigDecimal;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException(BigDecimal amount) {
        super("The amount " + amount.toString() + " is not a valid amount to make a transfer");
    }
}
