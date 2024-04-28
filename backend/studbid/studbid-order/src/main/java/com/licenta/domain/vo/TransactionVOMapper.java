package com.licenta.domain.vo;

import com.licenta.domain.Project;
import com.licenta.domain.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionVOMapper {
    public TransactionVO getVOFromEntity(Transaction transaction) {
        return new TransactionVO(
                transaction.getId(),
                transaction.getAnnouncement().getTitle(),
                (transaction.getAnnouncement() instanceof Project) ? transaction.getSkill().getSkill() : null,
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCreatedAt(),
                transaction.getSecondUser().getFirstName() + " " + transaction.getSecondUser().getLastName());
    }
}
