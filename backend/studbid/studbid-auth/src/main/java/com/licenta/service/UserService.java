package com.licenta.service;

import com.licenta.service.dto.UserDTO;

public interface UserService {
    UserDTO save(UserDTO userDTO);

    AuthenticationResponse login(UserDTO userDTO);

    boolean isExisting(UserDTO userDTO0);
}
