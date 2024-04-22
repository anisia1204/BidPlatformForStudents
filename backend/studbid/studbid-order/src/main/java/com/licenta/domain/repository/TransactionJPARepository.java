package com.licenta.domain.repository;

import com.licenta.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJPARepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAll(Specification<Transaction> specification, Pageable pageable);
}
