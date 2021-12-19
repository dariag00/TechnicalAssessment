package com.bsd.technicalassessment.account.service.service;

import com.bsd.technicalassessment.account.service.exception.AccountAlreadyExistsException;
import com.bsd.technicalassessment.account.service.exception.AccountNotFoundException;
import com.bsd.technicalassessment.account.service.repository.AccountRepository;
import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.AccountCreationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void findAll() {
        List<Account> expected = List.of(new Account(), new Account());

        Mockito.when(accountRepository.findAll()).thenReturn(expected);

        List<Account> accounts = accountService.findAll();

        Mockito.verify(accountRepository).findAll();
        Assertions.assertEquals(expected, accounts);
    }

    @Test
    void findById() {
        Account account = Account.builder().id(1L).build();

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.findById(1L);

        Mockito.verify(accountRepository).findById(1L);
        Assertions.assertEquals(result, account);
    }

    @Test
    void findById_notFound() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> accountService.findById(1L));

        Mockito.verify(accountRepository).findById(1L);
    }

    @Test
    void findByIBAN() {
        Account account = Account.builder().id(1L).iban("ES00").build();

        Mockito.when(accountRepository.findByIban("ES00")).thenReturn(Optional.of(account));

        Account result = accountService.findByIBAN("ES00");

        Mockito.verify(accountRepository).findByIban("ES00");
        Assertions.assertEquals(result, account);
    }

    @Test
    void findByIBAN_notFound() {
        Mockito.when(accountRepository.findByIban("NOT_FOUND")).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> accountService.findByIBAN("NOT_FOUND"));

        Mockito.verify(accountRepository).findByIban("NOT_FOUND");
    }

    @Test
    void createAccount() {
        AccountCreationRequest account = new AccountCreationRequest("IBAN1", null);
        Account expectedAccount = Account.builder().iban(account.getIban()).balance(BigDecimal.ZERO)
                .receivedTransfers(new ArrayList<>()).sentTransfers(new ArrayList<>()).build();

        Mockito.when(accountRepository.findByIban("IBAN1")).thenReturn(Optional.empty());

        accountService.createAccount(account);

        Mockito.verify(accountRepository).save(expectedAccount);
    }

    @Test
    void createAccount_ibanAlreadyExists() {
        AccountCreationRequest account = new AccountCreationRequest("IBAN1", null);
        Mockito.when(accountRepository.findByIban("IBAN1")).thenReturn(Optional.of(new Account()));
        Assertions.assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(account));
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void save() {
        Account account = Account.builder().id(1L).build();

        accountService.save(account);

        Mockito.verify(accountRepository).save(account);
    }

}
