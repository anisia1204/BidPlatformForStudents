package com.licenta.domain.repository;

import com.licenta.domain.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCodeJPARepository extends JpaRepository<QRCode, Long> {
    QRCode findByUser_Id(Long userId);
}
