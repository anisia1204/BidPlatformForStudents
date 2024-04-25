package com.licenta.domain.vo;

import com.licenta.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserVOMapper {
    public UserVO getVOFromEntity(User user, ProfilePictureVO profilePictureVO, QRCodeVO qrCodeVO) {
        return new UserVO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPoints(),
            user.getCreatedAt(),
            profilePictureVO,
            qrCodeVO
        );
    }
}
