package com.bsd.technicalassessment.model.exception;

public class NotEnoughBalanceException extends RuntimeException {

    public NotEnoughBalanceException(Long accountId) {
        super("Account " + accountId + " does not have enough balance to perform the transaction");
    }
}
