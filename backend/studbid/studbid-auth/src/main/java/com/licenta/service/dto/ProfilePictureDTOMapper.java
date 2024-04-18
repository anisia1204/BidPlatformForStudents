package com.licenta.service.dto;

import com.licenta.domain.ProfilePicture;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component
public class ProfilePictureDTOMapper {
    public ProfilePicture getEntityFromFile(MultipartFile file){
        ProfilePicture profilePicture = new ProfilePicture();

        profilePicture.setName(file.getOriginalFilename());
        profilePicture.setSize(file.getSize());
        try {
            profilePicture.setFileContent(file.getBytes());
        }
        catch(IOException e) {
            throw new ArithmeticException("Can't upload file " + file.getOriginalFilename());
        }
        return profilePicture;
    }

    public ProfilePictureDTO getDTOFromEntity(ProfilePicture profilePicture) {
        ProfilePictureDTO profilePictureDTO = new ProfilePictureDTO();

        profilePictureDTO.setId(profilePicture.getId());
        profilePictureDTO.setName(profilePicture.getName());
        profilePictureDTO.setUserId(profilePicture.getUser().getId());
        profilePictureDTO.setSize(profilePicture.getSize());
        profilePictureDTO.setFileContent(profilePicture.getFileContent());
        profilePictureDTO.setBase64EncodedStringOfFileContent(Base64.getEncoder().encodeToString(profilePicture.getFileContent()));

        return profilePictureDTO;
    }
}
