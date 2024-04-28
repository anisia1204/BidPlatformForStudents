package com.licenta.service;

import com.licenta.domain.User;

public interface WithdrawService {
    void save(User user, Double points);
}
