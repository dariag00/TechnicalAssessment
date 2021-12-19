package com.bsd.technicalassessment.account.service.exception;

import com.bsd.technicalassessment.model.exception.NotEnoughBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler({AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundHandler(RuntimeException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({InvalidAmountException.class, InvalidTransferException.class, AccountAlreadyExistsException.class, NotEnoughBalanceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequestHandler(RuntimeException e) {
        return e.getMessage();
    }

}
