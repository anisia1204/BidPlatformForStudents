package com.licenta.service.dto;

import com.licenta.domain.User;
import org.springframework.stereotype.Component;

@Component
public class LoggedInUserDTOMapper {
    public LoggedInUserDTO getDTOFromEntity(User user) {
        LoggedInUserDTO loggedInUserDTO = new LoggedInUserDTO();

        loggedInUserDTO.setId(user.getId());
        loggedInUserDTO.setFirstName(user.getFirstName());
        loggedInUserDTO.setLastName(user.getLastName());
        loggedInUserDTO.setEmail(user.getEmail());
        loggedInUserDTO.setPoints(user.getPoints());

        return loggedInUserDTO;
    }
}
