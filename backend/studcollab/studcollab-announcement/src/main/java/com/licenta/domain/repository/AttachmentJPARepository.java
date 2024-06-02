package com.licenta.domain.repository;

import com.licenta.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentJPARepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByTeachingMaterialId(Long teachingMaterialId);
    List<Attachment> findAllByTeachingMaterialIdAndDeletedEquals(Long teachingMaterialId, boolean deleted);
}
