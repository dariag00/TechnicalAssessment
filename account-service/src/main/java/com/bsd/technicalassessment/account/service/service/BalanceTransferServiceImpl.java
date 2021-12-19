package com.bsd.technicalassessment.account.service.service;

import com.bsd.technicalassessment.account.service.exception.InvalidAmountException;
import com.bsd.technicalassessment.account.service.exception.InvalidTransferException;
import com.bsd.technicalassessment.account.service.repository.TransferRepository;
import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.Transfer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Log4j2
@Service
public class BalanceTransferServiceImpl implements BalanceTransferService {

    private final AccountService accountService;
    private final TransferRepository transferRepository;

    public BalanceTransferServiceImpl(AccountService accountService, TransferRepository transferRepository) {
        this.accountService = accountService;
        this.transferRepository = transferRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Transfer transfer(Account from, Account to, BigDecimal amount) {
        log.info("Creating a transfer between account {} and {} with an amount of {}", from.getId(), to.getId(), amount);
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("The amount is less than 0. Amount: {}", amount);
            throw new InvalidAmountException(amount);
        }
        if(from.equals(to)) {
            log.warn("Both accounts {} and {} are equals.", from.getId(), to.getId());
            throw new InvalidTransferException("Cannot transfer money between the same account");
        }
        return performTransaction(from, to, amount);
    }

    private Transfer performTransaction(Account from, Account to, BigDecimal amount) {
        Account first = from;
        Account second = to;
        if(first.getId() > second.getId()) {
            first = to;
            second = from;
        }
        synchronized (first.getId()){
            synchronized (second.getId()) {

                Transfer transfer = new Transfer();
                transfer.setAmount(amount);
                transfer.setFromAccount(from);
                transfer.setToAccount(to);

                from.withdraw(amount);
                to.deposit(amount);
                Transfer persistedTransfer = transferRepository.save(transfer);

                from.addSentTransfers(persistedTransfer);
                accountService.save(from);

                to.addReceivedTransfers(persistedTransfer);
                accountService.save(to);

                log.info("Transfer performed between account {} and account {}", from.getId(), to.getId());

                return persistedTransfer;
            }
        }
    }


}
