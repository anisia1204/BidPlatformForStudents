package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.*;
import com.licenta.domain.repository.TransactionJPARepository;
import com.licenta.domain.vo.TransactionVO;
import com.licenta.domain.vo.TransactionVOMapper;
import com.licenta.service.email.EmailService;
import com.licenta.service.dto.TransactionDTO;
import com.licenta.service.dto.TransactionDTOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionJPARepository transactionJPARepository;
    private final AnnouncementService announcementService;
    private final SkillService skillService;
    private final UserService userService;
    private final TransactionDTOMapper transactionDTOMapper;
    private final TransactionVOMapper transactionVOMapper;
    private final EmailService emailService;
    private final TransactionFilterService transactionFilterService;

    public TransactionServiceImpl(TransactionJPARepository transactionJPARepository, AnnouncementService announcementService, SkillService skillService, UserService userService, TransactionDTOMapper transactionDTOMapper, TransactionVOMapper transactionVOMapper, EmailService emailService, TransactionFilterService transactionFilterService) {
        this.transactionJPARepository = transactionJPARepository;
        this.announcementService = announcementService;
        this.skillService = skillService;
        this.userService = userService;
        this.transactionDTOMapper = transactionDTOMapper;
        this.transactionVOMapper = transactionVOMapper;
        this.emailService = emailService;
        this.transactionFilterService = transactionFilterService;
    }

    @Override
    @Transactional
    public TransactionDTO buyTeachingMaterialOrTutoringService(TransactionDTO transactionDTO) {
        Announcement announcement = announcementService.markAsSold(transactionDTO.getAnnouncementId());
        LocalDateTime transactionCurrentDateAndTime = LocalDateTime.now();
        User buyer = getBuyer();
        User seller = getSellerOfAnnouncement(announcement);

        Transaction buyerTransaction = getSpendTransactionEntity(announcement, transactionCurrentDateAndTime, buyer);
        buyerTransaction.setSecondUser(seller);
        transactionJPARepository.saveAndFlush(buyerTransaction);

        Transaction sellerTransaction = getEarnTransactionEntity(announcement, transactionCurrentDateAndTime, seller);
        sellerTransaction.setSecondUser(buyer);
        transactionJPARepository.saveAndFlush(sellerTransaction);

        updateUserPoints(buyer, buyerTransaction.getAmount() + buyer.getPoints());
        updateUserPoints(seller, sellerTransaction.getAmount() + seller.getPoints());

        return transactionDTOMapper.getDTOFromEntity(buyerTransaction);
    }

    @Override
    @Transactional
    public List<TransactionDTO> buyProject(TransactionDTO transactionDTO) {
        LocalDateTime transactionCurrentDateAndTime = LocalDateTime.now();

        markSkillsAsSold(transactionDTO);

        Project project = (Project) announcementService.getById(transactionDTO.getAnnouncementId());
        if(allSkillsAreSold(project))
            announcementService.markAsSold(project.getId());

        List<Transaction> buyerTransactions = saveSkillsEarnTransactions(transactionDTO, project, transactionCurrentDateAndTime);
        List<Transaction> sellerTransactions = saveSkillsSpendTransactions(transactionDTO, project, transactionCurrentDateAndTime);

        User buyer = getBuyer();
        updateUserPoints(buyer, getAmountOfSkills(buyerTransactions) + buyer.getPoints());
        User seller = getSellerOfAnnouncement(project);
        updateUserPoints(seller, getAmountOfSkills(sellerTransactions) + seller.getPoints());

        return buyerTransactions
                .stream()
                .map(transactionDTOMapper::getDTOFromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionVO> getMyTransactions(String announcementTitle, Double amount, String createdAt, Long id, String skill, Long type, String secondUserFullName, Pageable pageable) {
        Specification<Transaction> specification = transactionFilterService.buildSpecificationForTransaction(announcementTitle, amount, createdAt, id, skill, type, secondUserFullName);
        return transactionJPARepository.findAll(specification, pageable).map(transactionVOMapper::getVOFromEntity);
    }

    private double getAmountOfSkills(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private List<Transaction> saveSkillsEarnTransactions(TransactionDTO transactionDTO, Project project, LocalDateTime transactionCurrentDateAndTime) {
        List<Transaction> buyerTransactions = new ArrayList<>();
        transactionDTO.getSkillIds()
                .stream()
                .map(skillService::getById)
                .forEach(skill -> {
                    Transaction buyerTransaction = getEarnTransactionEntity(project, transactionCurrentDateAndTime, getBuyer());
                    buyerTransaction.setSecondUser(getSellerOfAnnouncement(project));
                    buyerTransaction.setAmount(skill.getSkillPoints());
                    buyerTransaction.setSkill(skill);
                    transactionJPARepository.saveAndFlush(buyerTransaction);
                    buyerTransactions.add(buyerTransaction);
                });
        return buyerTransactions;
    }

    private List<Transaction> saveSkillsSpendTransactions(TransactionDTO transactionDTO, Project project, LocalDateTime transactionCurrentDateAndTime) {
        List<Transaction> sellerTransactions = new ArrayList<>();
        transactionDTO.getSkillIds()
                .stream()
                .map(skillService::getById)
                .forEach(skill -> {
                    Transaction sellerTransaction = getSpendTransactionEntity(project, transactionCurrentDateAndTime, getSellerOfAnnouncement(project));
                    sellerTransaction.setSecondUser(getBuyer());
                    sellerTransaction.setAmount(-skill.getSkillPoints());
                    sellerTransaction.setSkill(skill);
                    transactionJPARepository.saveAndFlush(sellerTransaction);
                    sellerTransactions.add(sellerTransaction);
                });
        return sellerTransactions;
    }

    private boolean allSkillsAreSold(Project project) {
        long soldSkillsOfProject = getSoldSkillsNotDeletedCountOf(project);
        return soldSkillsOfProject == getSizeOfNotDeletedSkills(project);
    }

    private int getSizeOfNotDeletedSkills(Project project) {
        return project.getRequiredSkills().stream().filter(skill -> !skill.getDeleted()).toList().size();
    }

    private long getSoldSkillsNotDeletedCountOf(Project project) {
        return project.getRequiredSkills()
                .stream()
                .filter(skill -> !skill.getDeleted())
                .filter(skill -> skill.getStatus() == SkillStatus.SOLD)
                .count();
    }


    private void markSkillsAsSold(TransactionDTO transactionDTO) {
        transactionDTO.getSkillIds()
                .stream()
                .map(skillService::getById)
                .forEach(skillService::markAsSold);
    }

    private Transaction  getSpendTransactionEntity(Announcement announcement, LocalDateTime transactionCurrentDateAndTime, User user) {
        Transaction transaction = new Transaction();
        transaction.setAnnouncement(announcement);
        transaction.setUser(user);
        transaction.setType(TransactionType.SPEND);
        if(!(announcement instanceof Project)){
            transaction.setAmount(-announcement.getPoints());
        }
        transaction.setCreatedAt(transactionCurrentDateAndTime);
        return transaction;
    }

    private User getBuyer() {
        return userService.findById(UserContextHolder.getUserContext().getUserId());
    }

    private Transaction getEarnTransactionEntity(Announcement announcement, LocalDateTime transactionCurrentDateAndTime, User user) {
        Transaction transaction = new Transaction();
        transaction.setAnnouncement(announcement);
        transaction.setUser(user);
        transaction.setType(TransactionType.EARN);
        if(!(announcement instanceof Project)) {
            transaction.setAmount(announcement.getPoints());
        }
        transaction.setCreatedAt(transactionCurrentDateAndTime);
        return transaction;
    }

    private User getSellerOfAnnouncement(Announcement announcement) {
        return announcement.getUser();
    }

    private void updateUserPoints(User user, Double amount) {
        userService.updateUserPoints(user, amount);
    }
}
