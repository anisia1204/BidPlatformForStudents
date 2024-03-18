package com.licenta.service;

import com.licenta.domain.vo.TransactionVO;
import com.licenta.service.dto.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TransactionService {
    TransactionDTO buyTeachingMaterialOrTutoringService(TransactionDTO transactionDTO);

    List<TransactionDTO> buyProject(TransactionDTO transactionDTO);

    Page<TransactionVO> getMyTransactions(String id,
                                           String createdAt,
                                           String amount,
                                           String title,
                                           int page,
                                           int size,
                                           List<String> sortList,
                                           String sortOrder);
}
