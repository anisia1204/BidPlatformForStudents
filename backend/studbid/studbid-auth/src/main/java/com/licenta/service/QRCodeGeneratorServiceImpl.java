package com.licenta.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.licenta.domain.QRCode;
import com.licenta.domain.User;
import com.licenta.domain.repository.QRCodeJPARepository;
import com.google.zxing.EncodeHintType;
import com.licenta.domain.vo.QRCodeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeGeneratorServiceImpl implements QRCodeGeneratorService{
    private final QRCodeJPARepository qrCodeJPARepository;

    public QRCodeGeneratorServiceImpl(QRCodeJPARepository qrCodeJPARepository) {
        this.qrCodeJPARepository = qrCodeJPARepository;
    }


    @Override
    @Transactional
    public void generateAndSaveQRCodeOfUser(User user) throws WriterException, IOException {

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                user.getId().toString(), BarcodeFormat.QR_CODE, 300, 300, hints
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);

        save(outputStream.toByteArray(), user);
    }

    @Override
    @Transactional(readOnly = true)
    public QRCodeVO getVOByUserId(Long id) {
        QRCode qrCode = qrCodeJPARepository.findByUser_Id(id);

        return (qrCode != null) ? new QRCodeVO(
                qrCode.getId(),
                encodeQRCodeToString(qrCode.getQrCode())) : null;
    }

    @Override
    @Transactional
    public void deleteQRCodeOfUser(String email) {
        QRCode qrCode = qrCodeJPARepository.findByUser_Email(email);
        qrCodeJPARepository.delete(qrCode);
    }

    private String encodeQRCodeToString(byte[] qrCode) {
        return Base64.getEncoder().encodeToString(qrCode);
    }

    private void save(byte[] qrCodeContent, User user) {
        QRCode qrCode = new QRCode();
        qrCode.setQrCode(qrCodeContent);
        qrCode.setUser(user);
        qrCodeJPARepository.save(qrCode);
    }
}
