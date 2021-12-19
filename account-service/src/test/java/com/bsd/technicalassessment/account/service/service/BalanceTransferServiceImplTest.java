package com.bsd.technicalassessment.account.service.service;

import com.bsd.technicalassessment.account.service.exception.InvalidAmountException;
import com.bsd.technicalassessment.account.service.exception.InvalidTransferException;
import com.bsd.technicalassessment.account.service.repository.TransferRepository;
import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.Transfer;
import com.bsd.technicalassessment.model.exception.NotEnoughBalanceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class BalanceTransferServiceImplTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BalanceTransferServiceImpl balanceTransferService;

    @Test
    void transfer() {
        Account fromAccount = Account.builder().id(1L).balance(BigDecimal.valueOf(1000)).receivedTransfers(new ArrayList<>()).sentTransfers(new ArrayList<>()).build();
        Account toAccount = Account.builder().id(2L).balance(BigDecimal.valueOf(500)).receivedTransfers(new ArrayList<>()).sentTransfers(new ArrayList<>()).build();
        BigDecimal amount = BigDecimal.valueOf(250);

        Transfer expectedTransfer = new Transfer(null, fromAccount, toAccount, amount, Instant.now());
        Account fromExpected = Account.builder().id(1L).balance(BigDecimal.valueOf(750)).receivedTransfers(new ArrayList<>()).sentTransfers(List.of(expectedTransfer)).build();
        Account toExpected = Account.builder().id(2L).balance(BigDecimal.valueOf(750)).receivedTransfers(List.of(expectedTransfer)).sentTransfers(new ArrayList<>()).build();

        Mockito.when(transferRepository.save(Mockito.any())).then(returnsFirstArg());

        balanceTransferService.transfer(fromAccount, toAccount, amount);

        Mockito.verify(accountService).save(fromExpected);
        Mockito.verify(accountService).save(toExpected);
    }

    @Test
    void transfer_invalidAmount() {
        Account fromAccount = Account.builder().id(1L).balance(BigDecimal.valueOf(1000)).receivedTransfers(new ArrayList<>()).sentTransfers(new ArrayList<>()).build();
        Account toAccount = Account.builder().id(2L).balance(BigDecimal.valueOf(500)).receivedTransfers(new ArrayList<>()).sentTransfers(new ArrayList<>()).build();

        Assertions.assertThrows(InvalidAmountException.class, () -> balanceTransferService.transfer(fromAccount, toAccount, BigDecimal.valueOf(-500)));

        Mockito.verify(accountService, Mockito.never()).save(Mockito.any(Account.class));
    }

    @Test
    void transfer_sameAccounts() {
        Account fromAccount = Account.builder().id(1L).balance(BigDecimal.valueOf(1000)).receivedTransfers(new ArrayList<>()).sentTransfers(new ArrayList<>()).build();

        Assertions.assertThrows(InvalidTransferException.class, () -> balanceTransferService.transfer(fromAccount, fromAccount, BigDecimal.valueOf(500)));

        Mockito.verify(accountService, Mockito.never()).save(Mockito.any(Account.class));
    }

    @Test
    void transfer_notEnoughBalance() {
        Account fromAccount = Account.builder().id(1L).balance(BigDecimal.valueOf(100)).receivedTransfers(new ArrayList<>()).sentTransfers(new ArrayList<>()).build();
        Account toAccount = Account.builder().id(2L).balance(BigDecimal.valueOf(500)).receivedTransfers(new ArrayList<>()).sentTransfers(new ArrayList<>()).build();

        Assertions.assertThrows(NotEnoughBalanceException.class, () -> balanceTransferService.transfer(fromAccount, toAccount, BigDecimal.valueOf(250)));

        Mockito.verify(accountService, Mockito.never()).save(Mockito.any(Account.class));
    }

}
