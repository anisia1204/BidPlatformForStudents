package com.licenta.service;


import com.licenta.domain.Transaction;
import org.springframework.data.jpa.domain.Specification;

public interface TransactionFilterService {
    Specification<Transaction> buildSpecificationForTransaction(String announcementTitle, Double amount, String createdAt, Long id, String skill, Long type, String secondUserFullName);
}
