package com.bsd.technicalassessment.account.service.service;

import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.Transfer;

import java.math.BigDecimal;

public interface BalanceTransferService {

    /**
     * Transfers the given money from one account to another
     * @param from account sending the money
     * @param to account receiving the money
     * @param amount sent amount
     */
    Transfer transfer(Account from, Account to, BigDecimal amount);

}
