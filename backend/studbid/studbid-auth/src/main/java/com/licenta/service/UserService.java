package com.licenta.service;

import com.licenta.domain.User;
import com.licenta.service.dto.UserDTO;

public interface UserService {
    UserDTO save(UserDTO userDTO);

    UserDTO login(UserDTO userDTO);

    boolean isExisting(UserDTO userDTO0);
    User getUserByEmail(String email);
}
