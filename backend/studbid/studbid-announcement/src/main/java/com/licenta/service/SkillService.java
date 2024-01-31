package com.licenta.service;

import com.licenta.domain.Project;
import com.licenta.service.dto.SkillDTO;

import java.util.List;

public interface SkillService {
    List<SkillDTO> saveAll(List<SkillDTO> projectSkillDTOS, Project project);
    SkillDTO save(SkillDTO skillDTO, Project project);
    List<SkillDTO> getAllDTOsByProjectId(Long projectId);
}
