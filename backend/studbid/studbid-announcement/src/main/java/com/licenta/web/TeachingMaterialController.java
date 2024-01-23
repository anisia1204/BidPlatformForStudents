package com.licenta.web;

import com.licenta.service.TeachingMaterialService;
import com.licenta.service.dto.TeachingMaterialDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teaching-material")
public class TeachingMaterialController {
    private final TeachingMaterialService teachingMaterialService;

    public TeachingMaterialController(TeachingMaterialService teachingMaterialService) {
        this.teachingMaterialService = teachingMaterialService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TeachingMaterialDTO> save(@RequestBody TeachingMaterialDTO teachingMaterialDTO, BindingResult bindingResult){
        return ResponseEntity.ok(teachingMaterialService.save(teachingMaterialDTO));
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<TeachingMaterialDTO> update(@RequestBody TeachingMaterialDTO teachingMaterialDTO, BindingResult bindingResult){
        return ResponseEntity.ok(teachingMaterialService.update(teachingMaterialDTO));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {
        teachingMaterialService.delete(id);
        return ResponseEntity.ok(true);
    }
}
