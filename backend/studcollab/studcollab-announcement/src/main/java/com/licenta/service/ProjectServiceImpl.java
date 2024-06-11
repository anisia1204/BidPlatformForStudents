package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.Project;
import com.licenta.domain.Skill;
import com.licenta.domain.SkillStatus;
import com.licenta.domain.repository.ProjectJPARepository;
import com.licenta.domain.vo.SkillVO;
import com.licenta.service.dto.ProjectDTO;
import com.licenta.service.dto.ProjectDTOMapper;
import com.licenta.service.dto.SkillDTO;
import com.licenta.service.exception.ProjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService{
    private final ProjectJPARepository projectJPARepository;
    private final ProjectDTOMapper projectDTOMapper;
    private final SkillService skillService;

    public ProjectServiceImpl(ProjectJPARepository projectJPARepository, ProjectDTOMapper projectDTOMapper, SkillService skillService) {
        this.projectJPARepository = projectJPARepository;
        this.projectDTOMapper = projectDTOMapper;
        this.skillService = skillService;
    }

    @Override
    @Transactional
    public ProjectDTO save(ProjectDTO projectDTO) {
        projectDTO.setUserId(UserContextHolder.getUserContext().getUserId());
        Project project =  projectDTOMapper.getEntityFromDTO(projectDTO);
        project.setStatus(AnnouncementStatus.ACTIVE);
        project.setCreatedAt(LocalDateTime.now());
        project.setDeleted(false);

        List<SkillDTO> projectSkillDTOS = projectDTO.getRequiredSkills();
        project.setPoints(skillService.getPointsSumOfSkills(projectSkillDTOS));
        projectJPARepository.save(project);
        projectSkillDTOS = saveRequiredSkills(projectSkillDTOS, project);

        projectDTO = projectDTOMapper.getDTOFromEntity(project);
        projectDTO.setRequiredSkills(projectSkillDTOS);

        return projectDTO;
    }

    private List<SkillDTO> saveRequiredSkills(List<SkillDTO> projectSkillDTOS, Project project) {
        return skillService.saveAll(projectSkillDTOS, project);
    }

    @Override
    @Transactional
    public ProjectDTO update(ProjectDTO projectDTO) {
        Project project = getById(projectDTO.getId());
        projectDTOMapper.updateEntityFields(project, projectDTO);

        List<Skill> existingSkills = project.getRequiredSkills();
        List<SkillDTO> skillDTOS = projectDTO.getRequiredSkills();
        updateRequiredSkills(existingSkills, skillDTOS, project);
        List<SkillDTO> updatedSkillDTOs = skillService.getAllDTOsNotDeletedByProjectId(project.getId());

        project.setPoints(skillService.getPointsSumOfSkills(updatedSkillDTOs));

        projectJPARepository.save(project);

        projectDTO = projectDTOMapper.getDTOFromEntity(project);
        projectDTO.setRequiredSkills(updatedSkillDTOs);
        return projectDTO;
    }
    private void updateRequiredSkills(List<Skill> existingSkills, List<SkillDTO> skillDTOS, Project project) {
        Set<Long> incomingSkillIds = skillDTOS.stream()
                .map(SkillDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingSkills.forEach(existingSkill -> {
            if (incomingSkillIds.contains(existingSkill.getId())) {
                SkillDTO skillDTO = skillDTOS.stream()
                        .filter(dto -> Objects.equals(dto.getId(), existingSkill.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Skill DTO not found for existing skill"));
                skillService.update(skillDTO);
            } else {
                if(!(existingSkill.getStatus() == SkillStatus.SOLD)){
                    skillService.delete(existingSkill.getId());
                }
            }
        });

        skillDTOS.forEach(skillDTO -> {
            if (skillDTO.getId() == null) {
                SkillDTO newSkillDTO = skillService.save(skillDTO, project);
                skillDTO.setId(newSkillDTO.getId());
            }
        });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Project project = getById(id);
        project.setDeleted(true);
        project.setStatus(AnnouncementStatus.INACTIVE);
        project.getRequiredSkills().forEach(skill -> skillService.delete(skill.getId()));
        projectJPARepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getById(Long id) {
        return projectJPARepository.findById(id).orElseThrow(ProjectNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getTemplate(Long id) {
        Project project = getById(id);
        ProjectDTO projectDTO = projectDTOMapper.getDTOFromEntity(project);
        projectDTO.setRequiredSkills(skillService.getAllDTOsNotDeletedByProjectId(id));
        return projectDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillVO> getSkillVOsByProjectId(Long id) {
        return skillService.getAllVOsNotDeletedByProjectId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllByUserId(Long userId) {
        return projectJPARepository.countAllByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllByUserIdAndStatusIsSold(Long userId) {
        return projectJPARepository.countAllByUserIdAndStatusIs(userId, AnnouncementStatus.SOLD);
    }
}
