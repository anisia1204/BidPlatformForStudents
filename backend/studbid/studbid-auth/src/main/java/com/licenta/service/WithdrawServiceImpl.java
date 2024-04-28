package com.licenta.service;

import com.licenta.domain.User;
import com.licenta.domain.Withdraw;
import com.licenta.domain.repository.WithdrawJPARepository;
import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.domain.vo.UserDetailsVOMapper;
import com.licenta.service.dto.UpdateUserPointsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WithdrawServiceImpl implements WithdrawService {
    private final UserDetailsVOMapper userDetailsVOMapper;
    private final WithdrawJPARepository withdrawJPARepository;
    private UserService userService;

    public WithdrawServiceImpl(UserDetailsVOMapper userDetailsVOMapper, WithdrawJPARepository withdrawJPARepository, UserService userService) {
        this.userService = userService;
        this.userDetailsVOMapper = userDetailsVOMapper;
        this.withdrawJPARepository = withdrawJPARepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsVO getUserDetails(Long userId) {
        User user = userService.findById(userId);
        return this.userDetailsVOMapper.getVOFromEntity(user);
    }

    @Override
    @Transactional
    public UpdateUserPointsDTO saveWithdrawAndUpdateUserPoints(UpdateUserPointsDTO updateUserPointsDTO) {
        User user = userService.findById(updateUserPointsDTO.getId());

        userService.updateUserPoints(user, user.getPoints() - updateUserPointsDTO.getPointsToWithdraw());

        saveWithdraw(user, updateUserPointsDTO.getPointsToWithdraw());

        updateUserPointsDTO.setCurrentSold(user.getPoints());
        return updateUserPointsDTO;
    }

    @Transactional
    public void saveWithdraw(User user, Double points) {
        Withdraw withdraw = new Withdraw();
        withdraw.setUser(user);
        withdraw.setCreatedAt(LocalDateTime.now());
        withdraw.setPoints(points);
        withdrawJPARepository.save(withdraw);
    }
}
