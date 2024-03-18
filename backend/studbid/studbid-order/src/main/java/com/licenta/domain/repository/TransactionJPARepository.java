package com.licenta.domain.repository;

import com.licenta.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TransactionJPARepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT " +
    "b from Transaction b " +
            "join b.announcement a " +
            "join b.user c " +
            "where b.id = :id " +
            "and b.createdAt = :createdAt " +
            "and b.amount = :amount " +
            "and UPPER(a.title) like concat('%', UPPER(:title), '%') " +
            "and c.id = :user"
    )
    Page<Transaction> getAll(@Param("id") Long id,@Param("createdAt") LocalDateTime createdAt,@Param("amount") Double amount,@Param("title") String title,@Param("user") Long user,Pageable pageable);
//Page<Transaction> findByUserIdAndIdContainingAndCreatedAtAndAmountContainingAndAnnouncement_TitleContainingIgnoreCase(
//        Long userId,
//        Long id,
//        LocalDateTime createdAt,
//        Double amount,
//        String title,
//        Pageable pageable
//);
}
