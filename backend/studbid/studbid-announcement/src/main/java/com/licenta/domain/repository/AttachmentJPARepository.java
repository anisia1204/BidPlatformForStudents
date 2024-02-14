package com.licenta.domain.repository;

import com.licenta.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentJPARepository extends JpaRepository<Attachment, Long> {
}
