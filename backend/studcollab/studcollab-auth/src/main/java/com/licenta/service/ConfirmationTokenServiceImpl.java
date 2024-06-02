package com.licenta.service;

import com.licenta.domain.ConfirmationToken;
import com.licenta.domain.repository.ConfirmationTokenJPARepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{
    private final ConfirmationTokenJPARepository confirmationTokenJPARepository;

    public ConfirmationTokenServiceImpl(ConfirmationTokenJPARepository confirmationTokenJPARepository) {
        this.confirmationTokenJPARepository = confirmationTokenJPARepository;
    }

    @Override
    @Transactional
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenJPARepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenJPARepository.findByToken(token);
    }

    @Override
    @Transactional
    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = getToken(token).orElseThrow();
        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public ConfirmationToken getTokenByUserId(Long id) {
        return confirmationTokenJPARepository.findByUser_Id(id);
    }

    @Override
    @Transactional
    public void deleteTokenOfUser(String email) {
        ConfirmationToken confirmationToken = confirmationTokenJPARepository.findByUser_Email(email);
        confirmationTokenJPARepository.delete(confirmationToken);
    }
}
