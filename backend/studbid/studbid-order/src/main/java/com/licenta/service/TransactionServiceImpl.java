package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.Announcement;
import com.licenta.domain.Transaction;
import com.licenta.domain.TransactionType;
import com.licenta.domain.User;
import com.licenta.domain.repository.TransactionJPARepository;
import com.licenta.service.dto.TransactionDTO;
import com.licenta.service.dto.TransactionDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionJPARepository transactionJPARepository;
    private final AnnouncementService announcementService;
    private final UserService userService;
    private final TransactionDTOMapper transactionDTOMapper;

    public TransactionServiceImpl(TransactionJPARepository transactionJPARepository, AnnouncementService announcementService, UserService userService, TransactionDTOMapper transactionDTOMapper) {
        this.transactionJPARepository = transactionJPARepository;
        this.announcementService = announcementService;
        this.userService = userService;
        this.transactionDTOMapper = transactionDTOMapper;
    }

    @Override
    @Transactional
    public TransactionDTO save(TransactionDTO transactionDTO) {
        Announcement announcement = announcementService.markAsSold(transactionDTO.getAnnouncementId());
        LocalDateTime transactionCurrentDateAndTime = LocalDateTime.now();

        Transaction buyerTransaction = saveBuyerTransaction(announcement, transactionCurrentDateAndTime);
        Transaction sellerTransaction = saveSellerTransaction(announcement, transactionCurrentDateAndTime);

        updateUserPoints(getBuyer(), buyerTransaction.getAmount());
        updateUserPoints(getSellerOfAnnouncement(announcement), sellerTransaction.getAmount());

        return transactionDTOMapper.getDTOFromEntity(buyerTransaction);
    }

    private Transaction saveBuyerTransaction(Announcement announcement, LocalDateTime transactionCurrentDateAndTime) {
        Transaction transaction = new Transaction();
        transaction.setAnnouncement(announcement);
        transaction.setUser(getBuyer());
        transaction.setType(TransactionType.SPEND);
        transaction.setAmount(-announcement.getPoints());
        transaction.setCreatedAt(transactionCurrentDateAndTime);
        return transactionJPARepository.saveAndFlush(transaction);
    }

    private User getBuyer() {
        return userService.findById(UserContextHolder.getUserContext().getUserId());
    }

    private Transaction saveSellerTransaction(Announcement announcement, LocalDateTime transactionCurrentDateAndTime) {
        Transaction transaction = new Transaction();
        transaction.setAnnouncement(announcement);
        transaction.setUser(getSellerOfAnnouncement(announcement));
        transaction.setType(TransactionType.EARN);
        transaction.setAmount(announcement.getPoints());
        transaction.setCreatedAt(transactionCurrentDateAndTime);
        return transactionJPARepository.saveAndFlush(transaction);
    }

    private User getSellerOfAnnouncement(Announcement announcement) {
        return announcement.getUser();
    }

    private void updateUserPoints(User user, Double amount) {
        userService.updateUserPoints(user, amount);
    }
}
