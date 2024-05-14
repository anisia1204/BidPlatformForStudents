package com.licenta.service;

import com.google.zxing.WriterException;
import com.licenta.domain.User;
import com.licenta.domain.vo.QRCodeVO;

import java.io.IOException;

public interface QRCodeGeneratorService {
    void generateAndSaveQRCodeOfUser(User user) throws WriterException, IOException;

    QRCodeVO getVOByUserId(Long id);

    void deleteQRCodeOfUser(String email);
}
