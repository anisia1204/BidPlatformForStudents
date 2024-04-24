package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionFilterServiceImpl implements TransactionFilterService {

    @Override
    public Specification<Transaction> buildSpecificationForTransaction(String announcementTitle, Double amount, String createdAt, Long id, String skill, Long type, String secondUserFullName) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("user").get("id"), UserContextHolder.getUserContext().getUserId());

            List<Predicate> predicates = new ArrayList<>();

            if (announcementTitle != null && !announcementTitle.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("announcement").get("title")), "%" + announcementTitle.toLowerCase() + "%"));
            }

            if (amount != null) {
                predicates.add(criteriaBuilder.equal(root.get("amount"), amount));
            }

            if (createdAt != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTime = LocalDateTime.parse(createdAt, formatter);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), dateTime));
            }

            if (id != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            }

            if (skill != null && !skill.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("skill").get("skill")), "%" + skill.toLowerCase() + "%"));
            }

            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            if (secondUserFullName != null && !secondUserFullName.isEmpty()) {
                Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("secondUser").get("firstName")), "%" + secondUserFullName.toLowerCase() + "%");
                Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("secondUser").get("lastName")), "%" + secondUserFullName.toLowerCase() + "%");

                predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate));
            }

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            return predicate;
        };
    }
}
