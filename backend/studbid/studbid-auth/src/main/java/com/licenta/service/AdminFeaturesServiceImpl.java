package com.licenta.service;

import com.licenta.domain.User;
import com.licenta.domain.repository.UserJPARepository;
import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.domain.vo.UserDetailsVOMapper;
import com.licenta.service.dto.UpdateUserPointsDTO;
import com.licenta.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminFeaturesServiceImpl implements AdminFeaturesService{
    private final UserJPARepository userJPARepository;
    private final UserDetailsVOMapper userDetailsVOMapper;


    public AdminFeaturesServiceImpl(UserJPARepository userJPARepository, UserDetailsVOMapper userDetailsVOMapper) {
        this.userJPARepository = userJPARepository;
        this.userDetailsVOMapper = userDetailsVOMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsVO getDetails(Long userId) {
        User user = getById(userId);
        return this.userDetailsVOMapper.getVOFromEntity(user);
    }

    @Override
    @Transactional
    public UpdateUserPointsDTO updateUserPoints(UpdateUserPointsDTO updateUserPointsDTO) {
        User user = getById(updateUserPointsDTO.getId());
        user.setPoints(user.getPoints() - updateUserPointsDTO.getPointsToWithdraw());
        this.userJPARepository.save(user);
        updateUserPointsDTO.setCurrentSold(user.getPoints());
        return updateUserPointsDTO;
    }

    private User getById(Long userId) {
        return this.userJPARepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
