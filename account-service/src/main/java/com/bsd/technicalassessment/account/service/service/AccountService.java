package com.bsd.technicalassessment.account.service.service;

import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.AccountCreationRequest;

import java.util.List;

public interface AccountService {

    /**
     * Returns every account
     * @return list of accounts stored on DB
     */
    List<Account> findAll();

    /**
     * Returns the requested account
     * @param id id of the account
     * @return requested account
     */
    Account findById(Long id);

    /**
     * Returns the requested account
     * @param iban iban of the account
     * @return requested account
     */
    Account findByIBAN(String iban);

    Account createAccount(AccountCreationRequest accountCreationRequest);

    /**
     * Saves the given account
     * @param account account to be saved
     * @return persisted account
     */
    Account save(Account account);
}
