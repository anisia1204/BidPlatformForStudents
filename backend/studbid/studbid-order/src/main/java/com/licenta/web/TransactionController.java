package com.licenta.web;

import com.licenta.service.TransactionService;
import com.licenta.service.dto.TransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionValidator transactionValidator;

    public TransactionController(TransactionService transactionService, TransactionValidator transactionValidator) {
        this.transactionService = transactionService;
        this.transactionValidator = transactionValidator;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> buy(@RequestBody TransactionDTO transactionDTO, BindingResult bindingResult) {
        transactionValidator.validate(transactionDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getCode();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(transactionService.save(transactionDTO));
    }
}
