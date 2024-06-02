package com.licenta.web;

import com.licenta.domain.vo.TransactionVO;
import com.licenta.service.TransactionService;
import com.licenta.service.dto.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<TransactionVO>> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String announcementTitle,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String createdAt,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Long type,
            @RequestParam(required = false) String secondUserFullName
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        return ResponseEntity.ok(transactionService.getMyTransactions(announcementTitle, amount, createdAt, id, skill, type, secondUserFullName, pageable));
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
        return ResponseEntity.ok(transactionService.buyTeachingMaterialOrTutoringService(transactionDTO));
    }

    @PostMapping(value = "/project")
    @ResponseBody
    public ResponseEntity<?> buyProject(@RequestBody TransactionDTO transactionDTO, BindingResult bindingResult) {
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
        return ResponseEntity.ok(transactionService.buyProject(transactionDTO));
    }
}
