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
    private final WithdrawService withdrawService;

    public AdminFeaturesServiceImpl(UserJPARepository userJPARepository, UserDetailsVOMapper userDetailsVOMapper, WithdrawService withdrawService) {
        this.userJPARepository = userJPARepository;
        this.userDetailsVOMapper = userDetailsVOMapper;
        this.withdrawService = withdrawService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsVO getDetails(Long userId) {
        User user = getById(userId);
        return this.userDetailsVOMapper.getVOFromEntity(user);
    }

    @Override
    @Transactional
    public UpdateUserPointsDTO updateUserPointsAndSaveWithdraw(UpdateUserPointsDTO updateUserPointsDTO) {
        User user = getById(updateUserPointsDTO.getId());
        user.setPoints(user.getPoints() - updateUserPointsDTO.getPointsToWithdraw());
        this.userJPARepository.save(user);

        saveWithdraw(user, updateUserPointsDTO.getPointsToWithdraw());

        updateUserPointsDTO.setCurrentSold(user.getPoints());
        return updateUserPointsDTO;
    }

    private void saveWithdraw(User user, Double pointsToWithdraw) {
        this.withdrawService.save(user, pointsToWithdraw);
    }

    private User getById(Long userId) {
        return this.userJPARepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
