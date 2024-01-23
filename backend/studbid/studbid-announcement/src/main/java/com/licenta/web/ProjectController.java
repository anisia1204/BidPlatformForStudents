package com.licenta.web;

import com.licenta.service.ProjectService;
import com.licenta.service.dto.ProjectDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProjectDTO> save(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.save(projectDTO));
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<ProjectDTO> update(@RequestBody ProjectDTO projectDTO, BindingResult bindingResult){
        return ResponseEntity.ok(projectService.update(projectDTO));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.ok(true);
    }
}
