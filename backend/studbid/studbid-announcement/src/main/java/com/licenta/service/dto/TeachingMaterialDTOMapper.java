package com.licenta.service.dto;

import com.licenta.domain.TeachingMaterial;
import com.licenta.service.UserService;
import org.springframework.stereotype.Component;


@Component
public class TeachingMaterialDTOMapper {
    private final UserService userService;

    public TeachingMaterialDTOMapper(UserService userService) {
        this.userService = userService;
    }

    public TeachingMaterialDTO getDTOFromEntity(TeachingMaterial teachingMaterial){
        TeachingMaterialDTO teachingMaterialDTO = new TeachingMaterialDTO();

        teachingMaterialDTO.setId(teachingMaterial.getId());
        teachingMaterialDTO.setUserId(teachingMaterial.getUser().getId());
        teachingMaterialDTO.setTitle(teachingMaterial.getTitle());
        teachingMaterialDTO.setDescription(teachingMaterial.getDescription());
        teachingMaterialDTO.setPoints(teachingMaterial.getPoints());
        teachingMaterialDTO.setStatus(teachingMaterial.getStatus());
        teachingMaterialDTO.setCreatedAt(teachingMaterial.getCreatedAt());

        teachingMaterialDTO.setAuthor(teachingMaterial.getAuthor());
        teachingMaterialDTO.setName(teachingMaterial.getName());
        teachingMaterialDTO.setEdition(teachingMaterial.getEdition());

        return teachingMaterialDTO;
    }

    public TeachingMaterial getEntityFromDTO(TeachingMaterialDTO teachingMaterialDTO){
        TeachingMaterial teachingMaterial = new TeachingMaterial();

        teachingMaterial.setUser(userService.findById(teachingMaterialDTO.getUserId()));
        updateEntityFields(teachingMaterial, teachingMaterialDTO);

        return teachingMaterial;
    }

    public void updateEntityFields(TeachingMaterial teachingMaterial, TeachingMaterialDTO teachingMaterialDTO) {
        teachingMaterial.setTitle(teachingMaterialDTO.getTitle());
        teachingMaterial.setDescription(teachingMaterialDTO.getDescription());
        teachingMaterial.setPoints(teachingMaterialDTO.getPoints());
        teachingMaterial.setAuthor(teachingMaterialDTO.getAuthor());
        teachingMaterial.setName(teachingMaterialDTO.getName());
        teachingMaterial.setEdition(teachingMaterialDTO.getEdition());
    }
}
