package com.licenta.service;

import com.licenta.domain.User;
import com.licenta.domain.repository.UserJPARepository;
import com.licenta.service.dto.UserDTO;
import com.licenta.service.dto.UserDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService{

    private final UserJPARepository userJPARepository;
    private final UserDTOMapper userDTOMapper;

    public UserServiceImpl(UserJPARepository userJPARepository, UserDTOMapper userDTOMapper) {
        this.userJPARepository = userJPARepository;
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    @Transactional
    public UserDTO save(UserDTO userDTO) {
        User user = userDTOMapper.getEntityFromDTO(userDTO);

        user.setPoints(100.0);
        userJPARepository.save(user);

        userDTO = userDTOMapper.getDTOFromEntity(user);

        return userDTO;
    }
}
