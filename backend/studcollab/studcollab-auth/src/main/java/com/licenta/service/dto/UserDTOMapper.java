package com.licenta.service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licenta.domain.Role;
import com.licenta.domain.User;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserDTOMapper {
    public User getEntityFromDTO(UserDTO userDTO) {
        User user = new User();

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

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
        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }

    public UserDTO getDTOFromString(String userDTOString) {
        UserDTO userDTO = new UserDTO();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            userDTO = objectMapper.readValue(userDTOString, UserDTO.class);
        } catch (IOException e) {
            System.out.print("Error");
        }
        return userDTO;
    }
}
