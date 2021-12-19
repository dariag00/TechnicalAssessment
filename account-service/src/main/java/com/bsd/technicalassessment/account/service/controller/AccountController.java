package com.bsd.technicalassessment.account.service.controller;

import com.bsd.technicalassessment.account.service.service.AccountService;
import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.AccountCreationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @Operation(description = "Returns the list of account")
    public ResponseEntity<List<Account>> list() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(description = "Returns the requested account")
    public ResponseEntity<Account> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @PostMapping
    @Operation(description = "Creates an account with the provided details")
    public ResponseEntity<Account> createAccount(@RequestBody @Valid AccountCreationRequest accountCreationRequest) {
        return new ResponseEntity<>(accountService.createAccount(accountCreationRequest), HttpStatus.CREATED);
    }

}
