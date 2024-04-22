package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.*;
import com.licenta.domain.repository.TransactionJPARepository;
import com.licenta.domain.vo.TransactionVO;
import com.licenta.domain.vo.TransactionVOMapper;
import com.licenta.email.EmailService;
import com.licenta.service.dto.TransactionDTO;
import com.licenta.service.dto.TransactionDTOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final EmailService emailService;

    public TransactionServiceImpl(TransactionJPARepository transactionJPARepository, AnnouncementService announcementService, SkillService skillService, UserService userService, TransactionDTOMapper transactionDTOMapper, TransactionVOMapper transactionVOMapper, EmailService emailService) {
        this.transactionJPARepository = transactionJPARepository;
        this.announcementService = announcementService;
        this.skillService = skillService;
        this.userService = userService;
        this.transactionDTOMapper = transactionDTOMapper;
        this.transactionVOMapper = transactionVOMapper;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public TransactionDTO buyTeachingMaterialOrTutoringService(TransactionDTO transactionDTO) {
        Announcement announcement = announcementService.markAsSold(transactionDTO.getAnnouncementId());
        LocalDateTime transactionCurrentDateAndTime = LocalDateTime.now();

        Transaction buyerTransaction = getSpendTransactionEntity(announcement, transactionCurrentDateAndTime, getBuyer());
        buyerTransaction.setSecondUser(getSellerOfAnnouncement(announcement));
        transactionJPARepository.saveAndFlush(buyerTransaction);

        Transaction sellerTransaction = getEarnTransactionEntity(announcement, transactionCurrentDateAndTime, getSellerOfAnnouncement(announcement));
        sellerTransaction.setSecondUser(getBuyer());
        transactionJPARepository.saveAndFlush(sellerTransaction);

        updateUserPoints(getBuyer(), buyerTransaction.getAmount());
        updateUserPoints(getSellerOfAnnouncement(announcement), sellerTransaction.getAmount());

        User seller = getSellerOfAnnouncement(announcement);
        sendConfirmationEmailForTeachingMaterialOrTutoringService(seller.getEmail(), seller.getFirstName(), announcement.getTitle());
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
    public Page<TransactionVO> getMyTransactions(String announcementTitle, Double amount, String createdAt, Long id, String skill, Long type, String secondUserFullName, Pageable pageable) {
        Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
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

    private void sendConfirmationEmailForTeachingMaterialOrTutoringService(String email, String firstName, String announcementTitle) {
        String subject = "Anuntul tau a fost cumparat!";
        emailService.send(
                email,
                buildEmail(firstName, announcementTitle),
                subject);
    }

    private void sendConfirmationEmailForProject(String email, String firstName, String announcementTitle) {
        String subject = "Anuntul tau a fost cumparat!";
        emailService.send(
                email,
                buildEmail(firstName, announcementTitle),
                subject);
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Anunt vandut!</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Salut " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Anuntul tau \"" + link + "\" tocmai a fost vandut</p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
