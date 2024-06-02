package com.licenta.web;

import com.licenta.service.ProjectService;
import com.licenta.service.dto.ProjectDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;
    private final AnnouncementUpdateValidator announcementUpdateValidator;

    public ProjectController(ProjectService projectService, AnnouncementUpdateValidator announcementUpdateValidator) {
        this.projectService = projectService;
        this.announcementUpdateValidator = announcementUpdateValidator;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProjectDTO> save(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.save(projectDTO));
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody ProjectDTO projectDTO, BindingResult bindingResult){
        announcementUpdateValidator.validate(projectDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String errorMessage = error.getDefaultMessage();
                errors.put("status", errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(projectService.update(projectDTO));
    }

    @GetMapping(value = {"/template/{id}"})
    @ResponseBody
    public ResponseEntity<?> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getTemplate(id));
    }

    @GetMapping(value = {"/{id}"})
    @ResponseBody
    public ResponseEntity<?> getSkillVOsByProjectId(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getSkillVOsByProjectId(id));
    }
}
