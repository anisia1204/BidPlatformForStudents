package com.licenta.service;

import com.licenta.domain.Project;
import com.licenta.service.dto.ProjectDTO;

public interface ProjectService {
    ProjectDTO save(ProjectDTO projectDTO);

    ProjectDTO update(ProjectDTO projectDTO);

    void delete(Long id);
    Project getById(Long id);
}
