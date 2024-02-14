package com.licenta.web;

import com.licenta.service.TeachingMaterialService;
import com.licenta.service.dto.TeachingMaterialDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/teaching-material")
public class TeachingMaterialController {
    private final TeachingMaterialService teachingMaterialService;

    public TeachingMaterialController(TeachingMaterialService teachingMaterialService) {
        this.teachingMaterialService = teachingMaterialService;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    public ResponseEntity<TeachingMaterialDTO> save(@RequestPart String teachingMaterialDTO, @RequestPart("files") MultipartFile[] files){
        return ResponseEntity.ok(teachingMaterialService.save(teachingMaterialDTO, files));
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    public ResponseEntity<TeachingMaterialDTO> update(@RequestPart String teachingMaterialDTO, @RequestPart("files") MultipartFile[] files){
        return ResponseEntity.ok(teachingMaterialService.update(teachingMaterialDTO, files));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {
        teachingMaterialService.delete(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping(value = {"/template/{id}"})
    @ResponseBody
    public ResponseEntity<?> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(teachingMaterialService.getTemplate(id));
    }
}
