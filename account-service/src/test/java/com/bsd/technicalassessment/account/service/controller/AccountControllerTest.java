package com.bsd.technicalassessment.account.service.controller;

import com.bsd.technicalassessment.account.service.service.AccountService;
import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.AccountCreationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void list() {
        List<Account> expected = List.of(new Account(), new Account());

        Mockito.when(accountService.findAll()).thenReturn(expected);

        ResponseEntity<List<Account>> responseEntity= accountController.list();

        Mockito.verify(accountService).findAll();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expected, responseEntity.getBody());
    }

    @Test
    void findById() {
        Account account = new Account();

        Mockito.when(accountService.findById(1L)).thenReturn(account);

        ResponseEntity<Account> responseEntity = accountController.findById(1L);

        Mockito.verify(accountService).findById(1L);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(account, responseEntity.getBody());
    }

    @Test
    void createAccount() {
        AccountCreationRequest account = new AccountCreationRequest("IBAN1", null);

        accountController.createAccount(account);

        Mockito.verify(accountService).createAccount(account);
    }

}
