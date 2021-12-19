package com.bsd.technicalassessment.account.service.repository;

import com.bsd.technicalassessment.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
