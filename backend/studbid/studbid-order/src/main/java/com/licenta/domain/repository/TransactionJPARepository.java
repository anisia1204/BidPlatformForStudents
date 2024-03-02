package com.licenta.domain.repository;

import com.licenta.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJPARepository extends JpaRepository<Transaction, Long> {
}
