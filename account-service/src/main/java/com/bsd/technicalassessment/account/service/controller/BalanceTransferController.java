package com.bsd.technicalassessment.account.service.controller;

import com.bsd.technicalassessment.account.service.service.AccountService;
import com.bsd.technicalassessment.account.service.service.BalanceTransferService;
import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.Transfer;
import com.bsd.technicalassessment.model.TransferRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/transfer")
@Tag(name = "Transfer")
public class BalanceTransferController {

    private final BalanceTransferService balanceTransferService;
    private final AccountService accountService;

    @Autowired
    public BalanceTransferController(BalanceTransferService balanceTransferService, AccountService accountService) {
        this.balanceTransferService = balanceTransferService;
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(description = "Submits a money transfer request between two accounts")
    public ResponseEntity<Transfer> transferMoney(@RequestBody @Valid TransferRequest transferRequest) {
        Account fromAccount = accountService.findByIBAN(transferRequest.getFromIban());
        Account toAccount = accountService.findByIBAN(transferRequest.getToIban());
        return new ResponseEntity<>(balanceTransferService.transfer(fromAccount, toAccount, transferRequest.getAmount()), HttpStatus.CREATED);
    }

}
