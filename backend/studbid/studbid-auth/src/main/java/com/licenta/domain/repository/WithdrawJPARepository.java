package com.licenta.domain.repository;

import com.licenta.domain.Withdraw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawJPARepository extends JpaRepository<Withdraw, Long> {
    Page<Withdraw> findAll(Specification<Withdraw> specification, Pageable pageable);
}
