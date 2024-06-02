package com.licenta.domain.repository;

import com.licenta.domain.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureJPARepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByUser_IdAndDeletedIsFalse(Long userId);
    ProfilePicture findByUser_Email(String email);
}
