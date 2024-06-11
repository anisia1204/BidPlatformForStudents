package com.licenta.service;

import com.licenta.domain.ProfilePicture;
import com.licenta.domain.User;
import com.licenta.domain.repository.ProfilePictureJPARepository;
import com.licenta.domain.vo.ProfilePictureVO;
import com.licenta.service.dto.ProfilePictureDTO;
import com.licenta.service.dto.ProfilePictureDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService{
    private final ProfilePictureDTOMapper profilePictureDTOMapper;
    private final ProfilePictureJPARepository profilePictureJPARepository;

    public ProfilePictureServiceImpl(ProfilePictureDTOMapper profilePictureDTOMapper, ProfilePictureJPARepository profilePictureJPARepository) {
        this.profilePictureDTOMapper = profilePictureDTOMapper;
        this.profilePictureJPARepository = profilePictureJPARepository;
    }

    @Override
    @Transactional
    public ProfilePictureDTO save(MultipartFile file, User user) {
        if(file != null) {
            ProfilePicture profilePicture = profilePictureDTOMapper.getEntityFromFile(file);
            profilePicture.setUser(user);
            profilePicture.setDeleted(false);
            profilePicture = profilePictureJPARepository.save(profilePicture);
            return profilePictureDTOMapper.getDTOFromEntity(profilePicture);
        }
        return null;
    }

    @Override
    @Transactional
    public ProfilePictureDTO update(MultipartFile file, User user) {
        ProfilePicture currentProfilePicture = getByUserId(user.getId());
        if(currentProfilePicture == null){
            return save(file, user);
        }
        ProfilePictureDTO profilePictureDTO = profilePictureDTOMapper.getDTOFromFile(file);
        currentProfilePicture.setSize(profilePictureDTO.getSize());
        currentProfilePicture.setName(profilePictureDTO.getName());
        currentProfilePicture.setFileContent(profilePictureDTO.getFileContent());
        return profilePictureDTOMapper.getDTOFromEntity(currentProfilePicture);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfilePictureVO getVOByUserId(Long userId) {
        ProfilePicture profilePicture = getByUserId(userId);
        return (profilePicture != null) ? new ProfilePictureVO(profilePicture.getId(),
                Base64.getEncoder().encodeToString(profilePicture.getFileContent())) : new ProfilePictureVO();
    }

    @Override
    @Transactional(readOnly = true)
    public String getBase64EncodedStringOfFileContentByUserId(Long userId) {
        ProfilePicture profilePicture = getByUserId(userId);
        return (profilePicture != null) ? Base64.getEncoder().encodeToString(profilePicture.getFileContent()) : null;
    }

    @Override
    @Transactional
    public void deleteProfilePictureOfUserIfExists(String email) {
        ProfilePicture profilePicture = profilePictureJPARepository.findByUser_Email(email);
        if(profilePicture != null){
            profilePictureJPARepository.delete(profilePicture);
        }
    }

    private ProfilePicture getByUserId(Long userId) {
        return profilePictureJPARepository.findByUser_IdAndDeletedIsFalse(userId);
    }
}
