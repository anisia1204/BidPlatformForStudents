package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.TutoringService;
import com.licenta.domain.repository.TutoringServiceJPARepository;
import com.licenta.service.dto.TutoringServiceDTO;
import com.licenta.service.dto.TutoringServiceDTOMapper;
import com.licenta.service.exception.TutoringServiceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TutoringServiceServiceImpl implements TutoringServiceService{
    private final TutoringServiceJPARepository tutoringServiceJPARepository;
    private final TutoringServiceDTOMapper tutoringServiceDTOMapper;

    public TutoringServiceServiceImpl(TutoringServiceJPARepository tutoringServiceJPARepository, TutoringServiceDTOMapper tutoringServiceDTOMapper) {
        this.tutoringServiceJPARepository = tutoringServiceJPARepository;
        this.tutoringServiceDTOMapper = tutoringServiceDTOMapper;
    }

    @Override
    @Transactional
    public TutoringServiceDTO save(TutoringServiceDTO tutoringServiceDTO) {
        tutoringServiceDTO.setUserId(UserContextHolder.getUserContext().getUserId());
        TutoringService tutoringService = tutoringServiceDTOMapper.getEntityFromDTO(tutoringServiceDTO);
        tutoringService.setStatus(AnnouncementStatus.ACTIVE);
        tutoringService.setCreatedAt(LocalDateTime.now());
        tutoringService.setDeleted(false);
        tutoringServiceJPARepository.saveAndFlush(tutoringService);
        tutoringServiceDTO = tutoringServiceDTOMapper.getDTOFromEntity(tutoringService);
        return tutoringServiceDTO;
    }

    @Override
    @Transactional
    public TutoringServiceDTO update(TutoringServiceDTO tutoringServiceDTO) {
        TutoringService tutoringService = getById(tutoringServiceDTO.getId());
        tutoringServiceDTOMapper.updateEntityFields(tutoringService, tutoringServiceDTO);
        tutoringServiceDTO = tutoringServiceDTOMapper.getDTOFromEntity(tutoringService);
        return tutoringServiceDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public TutoringService getById(Long id) {
        return tutoringServiceJPARepository.findById(id).orElseThrow(TutoringServiceNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public TutoringServiceDTO getTemplate(Long id) {
        TutoringService tutoringService = getById(id);
        return tutoringServiceDTOMapper.getDTOFromEntity(tutoringService);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        TutoringService tutoringService = getById(id);
        tutoringService.setDeleted(true);
        tutoringService.setStatus(AnnouncementStatus.INACTIVE);
        tutoringServiceJPARepository.save(tutoringService);
    }
}
