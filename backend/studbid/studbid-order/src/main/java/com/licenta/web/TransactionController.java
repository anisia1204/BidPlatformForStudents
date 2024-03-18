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
import java.util.List;
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
            @RequestParam(defaultValue = "") String id,
            @RequestParam(defaultValue = "") String createdAt,
            @RequestParam(defaultValue = "") String amount,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder
    ) {
        return ResponseEntity.ok(transactionService.getMyTransactions(id, createdAt, amount, title, page, size, sortList, sortOrder.toString()));
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
