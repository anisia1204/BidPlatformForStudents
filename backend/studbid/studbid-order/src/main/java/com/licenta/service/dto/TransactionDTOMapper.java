package com.licenta.service.dto;

import com.licenta.domain.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionDTOMapper {
    public TransactionDTO getDTOFromEntity(Transaction transaction){
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setUserId(transaction.getUser().getId());
        transactionDTO.setAnnouncementId(transaction.getAnnouncement().getId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setType(transaction.getType());
        transactionDTO.setCreatedAt(transaction.getCreatedAt());
        return transactionDTO;
    }
}
