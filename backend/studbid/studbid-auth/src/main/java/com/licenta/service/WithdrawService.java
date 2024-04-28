package com.licenta.service;

import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.service.dto.UpdateUserPointsDTO;

public interface WithdrawService {
    UserDetailsVO getUserDetails(Long userId);
    UpdateUserPointsDTO saveWithdrawAndUpdateUserPoints(UpdateUserPointsDTO updateUserPointsDTO);
}
