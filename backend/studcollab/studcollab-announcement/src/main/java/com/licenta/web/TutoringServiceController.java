package com.licenta.web;


import com.licenta.service.TutoringServiceService;
import com.licenta.service.dto.TutoringServiceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tutoring-service")
public class TutoringServiceController {
    private final TutoringServiceService tutoringServiceService;
    private final AnnouncementUpdateValidator announcementUpdateValidator;

    public TutoringServiceController(TutoringServiceService tutoringServiceService, AnnouncementUpdateValidator announcementUpdateValidator) {
        this.tutoringServiceService = tutoringServiceService;
        this.announcementUpdateValidator = announcementUpdateValidator;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TutoringServiceDTO> save(@RequestBody TutoringServiceDTO tutoringServiceDTO, BindingResult bindingResult){
        return ResponseEntity.ok(tutoringServiceService.save(tutoringServiceDTO));
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody TutoringServiceDTO tutoringServiceDTO, BindingResult bindingResult){
        announcementUpdateValidator.validate(tutoringServiceDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String errorMessage = error.getDefaultMessage();
                errors.put("status", errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(tutoringServiceService.update(tutoringServiceDTO));
    }

    @GetMapping(value = {"/template/{id}"})
    @ResponseBody
    public ResponseEntity<?> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(tutoringServiceService.getTemplate(id));
    }
}
