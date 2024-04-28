package com.licenta.service;

import com.licenta.domain.User;
import com.licenta.domain.Withdraw;
import com.licenta.domain.repository.WithdrawJPARepository;
import com.licenta.domain.vo.UserDetailsVO;
import com.licenta.domain.vo.UserDetailsVOMapper;
import com.licenta.domain.vo.WithdrawVO;
import com.licenta.service.dto.UpdateUserPointsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WithdrawServiceImpl implements WithdrawService {
    private final UserDetailsVOMapper userDetailsVOMapper;
    private final WithdrawJPARepository withdrawJPARepository;
    private final UserService userService;

    public WithdrawServiceImpl(UserDetailsVOMapper userDetailsVOMapper, WithdrawJPARepository withdrawJPARepository, UserService userService) {
        this.userService = userService;
        this.userDetailsVOMapper = userDetailsVOMapper;
        this.withdrawJPARepository = withdrawJPARepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsVO getUserDetails(Long userId) {
        User user = userService.findById(userId);
        return this.userDetailsVOMapper.getVOFromEntity(user);
    }

    @Override
    @Transactional
    public UpdateUserPointsDTO saveWithdrawAndUpdateUserPoints(UpdateUserPointsDTO updateUserPointsDTO) {
        User user = userService.findById(updateUserPointsDTO.getId());

        userService.updateUserPoints(user, user.getPoints() - updateUserPointsDTO.getPointsToWithdraw());

        saveWithdraw(user, updateUserPointsDTO.getPointsToWithdraw());

        updateUserPointsDTO.setCurrentSold(user.getPoints());
        return updateUserPointsDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WithdrawVO> getWithdrawHistory(String userFullName, Double points, String createdAt, Pageable pageable) {
        Specification<Withdraw> specification = buildSpecificationForWithdraw(userFullName, points, createdAt);
        return withdrawJPARepository.findAll(specification, pageable)
                .map(withdraw -> new WithdrawVO(
                        withdraw.getId(),
                        withdraw.getUser().getFirstName() + " " + withdraw.getUser().getLastName(),
                        withdraw.getPoints(),
                        withdraw.getCreatedAt()
                ));
    }

    private Specification<Withdraw> buildSpecificationForWithdraw(String userFullName, Double points, String createdAt) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userFullName != null && !userFullName.isEmpty()) {
                Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("firstName")), "%" + userFullName.toLowerCase() + "%");
                Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("lastName")), "%" + userFullName.toLowerCase() + "%");

                predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate));
            }

            if (points != null) {
                predicates.add(criteriaBuilder.equal(root.get("points"), points));
            }

            if (createdAt != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime dateTime = LocalDateTime.parse(createdAt, formatter);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), dateTime));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional
    public void saveWithdraw(User user, Double points) {
        Withdraw withdraw = new Withdraw();
        withdraw.setUser(user);
        withdraw.setCreatedAt(LocalDateTime.now());
        withdraw.setPoints(points);
        withdrawJPARepository.save(withdraw);
    }
}
