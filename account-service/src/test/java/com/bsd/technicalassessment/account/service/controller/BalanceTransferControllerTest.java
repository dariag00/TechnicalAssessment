package com.bsd.technicalassessment.account.service.controller;

import com.bsd.technicalassessment.account.service.service.AccountService;
import com.bsd.technicalassessment.account.service.service.BalanceTransferService;
import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.Transfer;
import com.bsd.technicalassessment.model.TransferRequest;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class BalanceTransferControllerTest {

    @Mock
    private BalanceTransferService balanceTransferService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private BalanceTransferController balanceTransferController;

    @Test
    void transferMoney() {
        TransferRequest transferRequest = new TransferRequest("IBAN1", "IBAN2", BigDecimal.valueOf(500));

        Account senderAccount = Account.builder().id(1L).iban("IBAN1").build();
        Account receiverAccount = Account.builder().id(2L).iban("IBAN2").build();

        Mockito.when(accountService.findByIBAN("IBAN1")).thenReturn(senderAccount);
        Mockito.when(accountService.findByIBAN("IBAN2")).thenReturn(receiverAccount);

        ResponseEntity<Transfer> transferResponseEntity = balanceTransferController.transferMoney(transferRequest);

        Mockito.verify(accountService).findByIBAN("IBAN1");
        Mockito.verify(accountService).findByIBAN("IBAN2");
        Mockito.verify(balanceTransferService).transfer(senderAccount, receiverAccount, BigDecimal.valueOf(500));

        Assertions.assertEquals(HttpStatus.CREATED, transferResponseEntity.getStatusCode());

    }

}
