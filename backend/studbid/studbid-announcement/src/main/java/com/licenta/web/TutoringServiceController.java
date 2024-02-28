package com.licenta.web;


import com.licenta.service.TutoringServiceService;
import com.licenta.service.dto.TutoringServiceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tutoring-service")
public class TutoringServiceController {
    private final TutoringServiceService tutoringServiceService;

    public TutoringServiceController(TutoringServiceService tutoringServiceService) {
        this.tutoringServiceService = tutoringServiceService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TutoringServiceDTO> save(@RequestBody TutoringServiceDTO tutoringServiceDTO, BindingResult bindingResult){
        return ResponseEntity.ok(tutoringServiceService.save(tutoringServiceDTO));
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<TutoringServiceDTO> update(@RequestBody TutoringServiceDTO tutoringServiceDTO, BindingResult bindingResult){
        return ResponseEntity.ok(tutoringServiceService.update(tutoringServiceDTO));
    }

    @GetMapping(value = {"/template/{id}"})
    @ResponseBody
    public ResponseEntity<?> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(tutoringServiceService.getTemplate(id));
    }
}
