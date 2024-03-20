package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.*;
import com.licenta.domain.repository.TransactionJPARepository;
import com.licenta.domain.vo.TransactionVO;
import com.licenta.domain.vo.TransactionVOMapper;
import com.licenta.service.dto.TransactionDTO;
import com.licenta.service.dto.TransactionDTOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionJPARepository transactionJPARepository;
    private final AnnouncementService announcementService;
    private final SkillService skillService;
    private final UserService userService;
    private final TransactionDTOMapper transactionDTOMapper;
    private final TransactionVOMapper transactionVOMapper;

    public TransactionServiceImpl(TransactionJPARepository transactionJPARepository, AnnouncementService announcementService, SkillService skillService, UserService userService, TransactionDTOMapper transactionDTOMapper, TransactionVOMapper transactionVOMapper) {
        this.transactionJPARepository = transactionJPARepository;
        this.announcementService = announcementService;
        this.skillService = skillService;
        this.userService = userService;
        this.transactionDTOMapper = transactionDTOMapper;
        this.transactionVOMapper = transactionVOMapper;
    }

    @Override
    @Transactional
    public TransactionDTO buyTeachingMaterialOrTutoringService(TransactionDTO transactionDTO) {
        Announcement announcement = announcementService.markAsSold(transactionDTO.getAnnouncementId());
        LocalDateTime transactionCurrentDateAndTime = LocalDateTime.now();

        Transaction buyerTransaction = getSpendTransactionEntity(announcement, transactionCurrentDateAndTime, getBuyer());
        transactionJPARepository.saveAndFlush(buyerTransaction);

        Transaction sellerTransaction = getEarnTransactionEntity(announcement, transactionCurrentDateAndTime, getSellerOfAnnouncement(announcement));
        transactionJPARepository.saveAndFlush(sellerTransaction);

        updateUserPoints(getBuyer(), buyerTransaction.getAmount());
        updateUserPoints(getSellerOfAnnouncement(announcement), sellerTransaction.getAmount());

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

        updateUserPoints(getBuyer(), getAmountOfSkills(buyerTransactions));
        updateUserPoints(getSellerOfAnnouncement(project), getAmountOfSkills(sellerTransactions));

        return buyerTransactions
                .stream()
                .map(transactionDTOMapper::getDTOFromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionVO> getMyTransactions(String id,
                                                 String createdAt,
                                                 String amount,
                                                 String title,
                                                 int page,
                                                 int size,
                                                 List<String> sortList,
                                                 String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        Page<Transaction> transactions = transactionJPARepository
                .getAll(

                        !Objects.equals(id, "") ? Long.valueOf(id) : null,
                        !Objects.equals(createdAt, "") ? LocalDateTime.parse(createdAt) : null,
                        !Objects.equals(amount, "") ? Double.valueOf(amount) : null,
                        title,
                        UserContextHolder.getUserContext().getUserId(),

                        pageable);
        return transactions.map(transactionVOMapper::getVOFromEntity);
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
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

    private Transaction getSpendTransactionEntity(Announcement announcement, LocalDateTime transactionCurrentDateAndTime, User user) {
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
