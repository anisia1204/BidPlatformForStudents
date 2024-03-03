package com.licenta.service;

import com.licenta.service.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO buyTeachingMaterialOrTutoringService(TransactionDTO transactionDTO);

    List<TransactionDTO> buyProject(TransactionDTO transactionDTO);
}
