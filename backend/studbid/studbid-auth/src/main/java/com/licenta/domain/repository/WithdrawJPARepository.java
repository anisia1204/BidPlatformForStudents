package com.licenta.domain.repository;

import com.licenta.domain.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawJPARepository extends JpaRepository<Withdraw, Long> {
}
