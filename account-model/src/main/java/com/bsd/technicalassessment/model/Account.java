package com.bsd.technicalassessment.model;

import com.bsd.technicalassessment.model.exception.NotEnoughBalanceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ACCOUNT")
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @NotEmpty
    @Column(name = "IBAN", nullable = false)
    private String iban;

    @NotNull
    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "fromAccount", cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private List<Transfer> sentTransfers;

    @OneToMany(mappedBy = "toAccount", cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private List<Transfer> receivedTransfers;

    @Version
    private Long version;

    public Account() {
        balance = BigDecimal.ZERO;
        sentTransfers = new ArrayList<>();
        receivedTransfers = new ArrayList<>();
    }

    public void addSentTransfers(Transfer transfer) {
        this.sentTransfers.add(transfer);
    }

    public void addReceivedTransfers(Transfer transfer) {
        this.receivedTransfers.add(transfer);
    }

    public void withdraw(BigDecimal amount) {
        if(this.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughBalanceException(this.getId());
        }
        BigDecimal currentFromBalance = this.getBalance().subtract(amount);
        this.setBalance(currentFromBalance);
    }

    public void deposit(BigDecimal amount) {
        BigDecimal currentToBalance = this.getBalance().add(amount);
        this.setBalance(currentToBalance);
    }

    @PrePersist
    @PreUpdate
    void updateRelationships() {
        sentTransfers.stream().filter(transfer -> transfer.getFromAccount() == null).forEach(transfer -> transfer.setFromAccount(this));
        receivedTransfers.stream().filter(transfer -> transfer.getToAccount() == null).forEach(transfer -> transfer.setToAccount(this));
    }
}
