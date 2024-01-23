package com.licenta.service;

import com.licenta.domain.TutoringService;
import com.licenta.service.dto.TutoringServiceDTO;

public interface TutoringServiceService {
    TutoringServiceDTO save(TutoringServiceDTO tutoringServiceDTO);
    TutoringServiceDTO update(TutoringServiceDTO tutoringServiceDTO);
    void delete(Long id);
    TutoringService getById(Long id);
}
