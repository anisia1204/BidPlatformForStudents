package com.licenta.service.dto;

import com.licenta.domain.TutoringService;
import com.licenta.service.UserService;
import org.springframework.stereotype.Component;


@Component
public class TutoringServiceDTOMapper {
    private final UserService userService;

    public TutoringServiceDTOMapper(UserService userService) {
        this.userService = userService;
    }

    public TutoringServiceDTO getDTOFromEntity(TutoringService tutoringService){
        TutoringServiceDTO tutoringServiceDTO = new TutoringServiceDTO();

        tutoringServiceDTO.setId(tutoringService.getId());
        tutoringServiceDTO.setUserId(tutoringService.getUser().getId());
        tutoringServiceDTO.setTitle(tutoringService.getTitle());
        tutoringServiceDTO.setDescription(tutoringService.getDescription());
        tutoringServiceDTO.setPoints(tutoringService.getPoints());
        tutoringServiceDTO.setStatus(tutoringService.getStatus());
        tutoringServiceDTO.setCreatedAt(tutoringService.getCreatedAt());

        tutoringServiceDTO.setSubject(tutoringService.getSubject());
        tutoringServiceDTO.setStartDate(tutoringService.getStartDate());
        tutoringServiceDTO.setEndDate(tutoringService.getEndDate());
        tutoringServiceDTO.setHoursPerSession(tutoringService.getHoursPerSession());
        tutoringServiceDTO.setTutoringType(tutoringService.getTutoringType());

        return tutoringServiceDTO;
    }

    public TutoringService getEntityFromDTO(TutoringServiceDTO tutoringServiceDTO){
        TutoringService tutoringService = new TutoringService();

        tutoringService.setUser(userService.findById(tutoringServiceDTO.getUserId()));
        updateEntityFields(tutoringService, tutoringServiceDTO);

        return tutoringService;
    }

    public void updateEntityFields(TutoringService tutoringService, TutoringServiceDTO tutoringServiceDTO) {
        tutoringService.setTitle(tutoringServiceDTO.getTitle());
        tutoringService.setDescription(tutoringServiceDTO.getDescription());
        tutoringService.setPoints(tutoringServiceDTO.getPoints());

        tutoringService.setSubject(tutoringServiceDTO.getSubject());
        tutoringService.setStartDate(tutoringServiceDTO.getStartDate());
        tutoringService.setEndDate(tutoringServiceDTO.getEndDate());
        tutoringService.setHoursPerSession(tutoringServiceDTO.getHoursPerSession());
        tutoringService.setTutoringType(tutoringServiceDTO.getTutoringType());
    }
}
