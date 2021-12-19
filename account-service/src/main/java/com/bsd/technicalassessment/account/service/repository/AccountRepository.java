package com.bsd.technicalassessment.account.service.repository;

import com.bsd.technicalassessment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIban(String accountNumber);
}
