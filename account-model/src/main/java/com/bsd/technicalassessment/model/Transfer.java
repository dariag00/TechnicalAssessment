package com.bsd.technicalassessment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "TRANSFER")
@AllArgsConstructor
@EqualsAndHashCode
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FROM_ACCOUNT")
    @JsonIgnoreProperties(value = {"sentTransfers", "receivedTransfers"})
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "TO_ACCOUNT")
    @JsonIgnoreProperties(value = {"sentTransfers", "receivedTransfers"})
    private Account toAccount;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "TRANSFER_DATE")
    private Instant transferDate;

    public Transfer() {
        transferDate = Instant.now();
    }
}
