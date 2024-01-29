package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.Project;
import com.licenta.domain.repository.ProjectJPARepository;
import com.licenta.service.dto.ProjectDTO;
import com.licenta.service.dto.ProjectDTOMapper;
import com.licenta.service.dto.SkillDTO;
import com.licenta.service.exception.ProjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        projectJPARepository.save(project);

        List<SkillDTO> projectSkillDTOS = projectDTO.getRequiredSkills();
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
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Project project = getById(id);
        project.setDeleted(true);
        projectJPARepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getById(Long id) {
        return projectJPARepository.findById(id).orElseThrow(ProjectNotFoundException::new);
    }
}
