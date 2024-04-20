package com.licenta.service;

import com.licenta.domain.Project;
import com.licenta.domain.Skill;
import com.licenta.domain.SkillStatus;
import com.licenta.domain.repository.SkillJPARepository;
import com.licenta.domain.vo.SkillVO;
import com.licenta.domain.vo.SkillVOMapper;
import com.licenta.service.dto.SkillDTO;
import com.licenta.service.dto.SkillDTOMapper;
import com.licenta.service.exception.SkillNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillServiceImpl implements SkillService{
    private final SkillJPARepository skillJPARepository;
    private final SkillDTOMapper skillDTOMapper;
    private final SkillVOMapper skillVOMapper;

    public SkillServiceImpl(SkillJPARepository skillJPARepository, SkillDTOMapper skillDTOMapper, SkillVOMapper skillVOMapper) {
        this.skillJPARepository = skillJPARepository;
        this.skillDTOMapper = skillDTOMapper;
        this.skillVOMapper = skillVOMapper;
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

    @Override
    @Transactional(readOnly = true)
    public List<SkillDTO> getAllDTOsNotDeletedByProjectId(Long projectId) {
        return skillJPARepository.findAllByProjectIdAndDeletedEquals(projectId, false)
                .stream()
                .map(skillDTOMapper::getDTOFromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillVO> getAllVOsNotDeletedByProjectId(Long projectId) {
        return skillJPARepository.findAllByProjectIdAndDeletedEqualsOrderByStatusAsc(projectId, false)
                .stream()
                .map(skillVOMapper::getVOFromEntity)
                .toList();
    }

    @Override
    @Transactional
    public SkillDTO update(SkillDTO skillDTO) {
        Skill skill = getById(skillDTO.getId());
        skillDTOMapper.updateEntityFields(skill, skillDTO);
        skillDTO = skillDTOMapper.getDTOFromEntity(skill);
        return skillDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Skill getById(Long id) {
        return skillJPARepository.findById(id).orElseThrow(SkillNotFoundException::new);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Skill skill = getById(id);
        skill.setDeleted(true);
        skillJPARepository.save(skill);
    }

    @Override
    public Double getPointsSumOfSkills(List<SkillDTO> projectSkillDTOS) {
        return projectSkillDTOS.stream().mapToDouble(SkillDTO::getSkillPoints).sum();
    }

    @Override
    @Transactional
    public Skill markAsSold(Skill skill) {
        skill.setStatus(SkillStatus.SOLD);
        skillJPARepository.save(skill);
        return skill;
    }
}
