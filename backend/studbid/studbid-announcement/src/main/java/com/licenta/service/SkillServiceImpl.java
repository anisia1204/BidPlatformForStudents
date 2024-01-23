package com.licenta.service;

import com.licenta.domain.Project;
import com.licenta.domain.Skill;
import com.licenta.domain.SkillStatus;
import com.licenta.domain.repository.SkillJPARepository;
import com.licenta.service.dto.SkillDTO;
import com.licenta.service.dto.SkillDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillServiceImpl implements SkillService{
    private final SkillJPARepository skillJPARepository;
    private final SkillDTOMapper skillDTOMapper;

    public SkillServiceImpl(SkillJPARepository skillJPARepository, SkillDTOMapper skillDTOMapper) {
        this.skillJPARepository = skillJPARepository;
        this.skillDTOMapper = skillDTOMapper;
    }

    @Override
    @Transactional
    public List<SkillDTO> saveAll(List<SkillDTO> projectSkillDTOS, Project project) {
        List<SkillDTO> skillDTOS = new ArrayList<>();
        projectSkillDTOS.forEach(skillDTO -> {
            skillDTO = save(skillDTO, project);
            skillDTOS.add(skillDTO);
        });
        return skillDTOS;
    }

    @Override
    @Transactional
    public SkillDTO save(SkillDTO skillDTO, Project project) {
        Skill skill = skillDTOMapper.getEntityFromDTO(skillDTO);
        skill.setProject(project);
        skill.setStatus(SkillStatus.ACTIVE);
        skill.setDeleted(false);
        skillJPARepository.save(skill);
        skillDTO = skillDTOMapper.getDTOFromEntity(skill);
        return skillDTO;
    }
}
