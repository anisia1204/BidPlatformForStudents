package com.licenta.service;

import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.domain.vo.WithdrawVO;
import com.licenta.service.dto.UpdateUserPointsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WithdrawService {
    UserDetailsVO getUserDetails(Long userId);
    UpdateUserPointsDTO saveWithdrawAndUpdateUserPoints(UpdateUserPointsDTO updateUserPointsDTO);
    Page<WithdrawVO> getWithdrawHistory(String userFullName, Double points, String createdAt, Pageable pageable);
}
