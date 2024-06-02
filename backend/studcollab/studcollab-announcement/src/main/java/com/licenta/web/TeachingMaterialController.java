package com.licenta.web;

import com.licenta.service.TeachingMaterialService;
import com.licenta.service.dto.TeachingMaterialDTO;
import com.licenta.service.dto.TeachingMaterialDTOMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/teaching-material")
public class TeachingMaterialController {
    private final TeachingMaterialService teachingMaterialService;
    private final AnnouncementUpdateValidator announcementUpdateValidator;
    private final TeachingMaterialDTOMapper teachingMaterialDTOMapper;


    public TeachingMaterialController(TeachingMaterialService teachingMaterialService, AnnouncementUpdateValidator announcementUpdateValidator, TeachingMaterialDTOMapper teachingMaterialDTOMapper) {
        this.teachingMaterialService = teachingMaterialService;
        this.announcementUpdateValidator = announcementUpdateValidator;
        this.teachingMaterialDTOMapper = teachingMaterialDTOMapper;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    public ResponseEntity<TeachingMaterialDTO> save(@RequestPart String teachingMaterialDTO, @RequestPart("files") MultipartFile[] files){
        return ResponseEntity.ok(teachingMaterialService.save(teachingMaterialDTO, files));
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    public ResponseEntity<?> update(@RequestPart String teachingMaterialDTOString, @RequestPart(value = "files", required = false) MultipartFile[] files, BindingResult bindingResult){
        TeachingMaterialDTO teachingMaterialDTO = teachingMaterialDTOMapper.getDTOFromString(teachingMaterialDTOString);
        announcementUpdateValidator.validate(teachingMaterialDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String errorMessage = error.getDefaultMessage();
                errors.put("status", errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(teachingMaterialService.update(teachingMaterialDTO, files));
    }

    @GetMapping(value = {"/template/{id}"})
    @ResponseBody
    public ResponseEntity<?> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(teachingMaterialService.getTemplate(id));
    }
}
