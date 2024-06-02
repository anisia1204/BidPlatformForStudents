package com.licenta.service.dto;

import com.licenta.domain.Project;
import com.licenta.service.UserService;
import org.springframework.stereotype.Component;


@Component
public class ProjectDTOMapper {
    private final UserService userService;

    public ProjectDTOMapper(UserService userService) {
        this.userService = userService;
    }

    public ProjectDTO getDTOFromEntity(Project project){
        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setId(project.getId());
        projectDTO.setUserId(project.getUser().getId());
        projectDTO.setTitle(project.getTitle());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setPoints(project.getPoints());
        projectDTO.setStatus(project.getStatus());
        projectDTO.setCreatedAt(project.getCreatedAt());
        projectDTO.setTeamSize(project.getTeamSize());
        projectDTO.setDomain(project.getDomain());
        projectDTO.setAnnouncementType("project");

        return projectDTO;
    }

    public Project getEntityFromDTO(ProjectDTO projectDTO){
        Project project = new Project();

        project.setUser(userService.findById(projectDTO.getUserId()));
        updateEntityFields(project, projectDTO);

        return project;
    }

    public void updateEntityFields(Project project, ProjectDTO projectDTO) {
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setTeamSize(projectDTO.getTeamSize());
        project.setDomain(projectDTO.getDomain());

    }
}
