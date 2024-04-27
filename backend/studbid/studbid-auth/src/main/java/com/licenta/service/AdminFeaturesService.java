package com.licenta.service;

import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.service.dto.UpdateUserPointsDTO;

public interface AdminFeaturesService {
    UserDetailsVO getDetails(Long userId);
    UpdateUserPointsDTO updateUserPoints(UpdateUserPointsDTO updateUserPointsDTO);
}
