package com.licenta.service.dto;

import com.licenta.domain.Role;
import com.licenta.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {
    public User getEntityFromDTO(UserDTO userDTO) {
        User user = new User();

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(Role.USER);

        return user;
    }

    public UserDTO getDTOFromEntity(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPoints(user.getPoints());

        return userDTO;
    }
}
