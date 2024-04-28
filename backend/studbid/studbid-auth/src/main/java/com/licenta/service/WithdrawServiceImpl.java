package com.licenta.service;

import com.licenta.domain.User;
import com.licenta.domain.Withdraw;
import com.licenta.domain.repository.WithdrawJPARepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WithdrawServiceImpl implements WithdrawService{
    private final WithdrawJPARepository withdrawJPARepository;

    public WithdrawServiceImpl(WithdrawJPARepository withdrawJPARepository) {
        this.withdrawJPARepository = withdrawJPARepository;
    }

    @Override
    @Transactional
    public void save(User user, Double points) {
        Withdraw withdraw = new Withdraw();
        withdraw.setUser(user);
        withdraw.setCreatedAt(LocalDateTime.now());
        withdraw.setPoints(points);
        withdrawJPARepository.save(withdraw);
    }
}
