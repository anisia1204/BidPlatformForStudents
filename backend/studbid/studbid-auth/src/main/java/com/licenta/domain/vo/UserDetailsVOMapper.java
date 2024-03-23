package com.licenta.domain.vo;

import com.licenta.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsVOMapper {
    public UserDetailsVO getVOFromEntity(User user) {
        return new UserDetailsVO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
