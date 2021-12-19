package com.bsd.technicalassessment.account.service.exception;

public class InvalidTransferException extends RuntimeException {

    public InvalidTransferException(String message) {
        super(message);
    }
}
