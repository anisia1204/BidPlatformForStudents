package com.licenta.domain.vo;

import com.licenta.domain.ProfilePicture;
import com.licenta.domain.User;
import com.licenta.domain.repository.ProfilePictureJPARepository;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class UserDetailsVOMapper {
    private final ProfilePictureJPARepository profilePictureJPARepository;

    public UserDetailsVOMapper(ProfilePictureJPARepository profilePictureJPARepository) {
        this.profilePictureJPARepository = profilePictureJPARepository;
    }

    public UserDetailsVO getVOFromEntity(User user) {
        return new UserDetailsVO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPoints(),
                getBase64EncodedStringOfFileContentByUserId(user.getId()));
    }

    private String getBase64EncodedStringOfFileContentByUserId(Long id) {
        ProfilePicture profilePicture = profilePictureJPARepository.findByUser_IdAndDeletedIsFalse(id);
        return (profilePicture != null) ? getEncodedFileContentFromProfilePicture(profilePicture) : null;
    }

    private String getEncodedFileContentFromProfilePicture(ProfilePicture profilePicture){
        return Base64.getEncoder().encodeToString(profilePicture.getFileContent());
    }
}
