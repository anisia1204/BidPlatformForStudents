package com.licenta.service;

import com.licenta.domain.Project;
import com.licenta.domain.vo.SkillVO;
import com.licenta.service.dto.ProjectDTO;
import java.util.List;

public interface ProjectService {
    ProjectDTO save(ProjectDTO projectDTO);

    ProjectDTO update(ProjectDTO projectDTO);

    void delete(Long id);
    Project getById(Long id);

    ProjectDTO getTemplate(Long id);

    List<SkillVO> getSkillVOsByProjectId(Long id);
    long countAllByUserId(Long userId);
    long countAllByUserIdAndStatusIsSold(Long userId);
}
