package com.licenta.service;

import com.licenta.domain.User;
import com.licenta.domain.vo.ProfilePictureVO;
import com.licenta.service.dto.ProfilePictureDTO;
import org.springframework.web.multipart.MultipartFile;


public interface ProfilePictureService {
    ProfilePictureDTO save(MultipartFile file, User user);
    ProfilePictureVO getVOByUserId(Long userId);
    String getBase64EncodedStringOfFileContentByUserId(Long userId);
    void deleteProfilePictureOfUserIfExists(String email);
}
