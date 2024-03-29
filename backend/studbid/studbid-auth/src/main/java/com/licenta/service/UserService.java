package com.licenta.service;

import com.licenta.domain.User;
import com.licenta.domain.vo.UserVO;
import com.licenta.service.dto.LoggedInUserDTO;
import com.licenta.service.dto.UserDTO;

public interface UserService {
    UserDTO save(UserDTO userDTO);

    LoggedInUserDTO login(UserDTO userDTO);

    boolean isExisting(UserDTO userDTO0);
    User getUserByEmail(String email);

    void confirmUser(String confirmationToken);

    User findById(Long userId);

    void updateUserPoints(User user, Double amount);

    UserVO getProfileInformation();

    UserDTO editProfileInformation(UserDTO userDTO);
    User getById(Long id);
}
