package com.licenta.service;

import com.licenta.domain.Project;
import com.licenta.domain.Skill;
import com.licenta.service.dto.SkillDTO;

import java.util.List;

public interface SkillService {
    List<SkillDTO> saveAll(List<SkillDTO> projectSkillDTOS, Project project);
    SkillDTO save(SkillDTO skillDTO, Project project);
    List<SkillDTO> getAllDTOsNotDeletedByProjectId(Long projectId);
    SkillDTO update(SkillDTO skillDTO);
    Skill getById(Long id);
    void delete(Long id);

    Double getPointsSumOfSkills(List<SkillDTO> projectSkillDTOS);
}
