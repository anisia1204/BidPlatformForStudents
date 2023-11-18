package com.licenta.service;

import com.licenta.service.dto.UserDTO;

public interface UserService {
    AuthenticationResponse save(UserDTO userDTO);

    AuthenticationResponse login(UserDTO userDTO);
}
