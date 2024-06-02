package com.licenta.service;

import com.google.zxing.WriterException;
import com.licenta.config.service.JwtService;
import com.licenta.context.UserContextHolder;
import com.licenta.domain.ConfirmationToken;
import com.licenta.domain.Role;
import com.licenta.domain.User;
import com.licenta.domain.repository.UserJPARepository;
import com.licenta.domain.vo.*;
import com.licenta.service.email.EmailSender;
import com.licenta.service.dto.LoggedInUserDTO;
import com.licenta.service.dto.LoggedInUserDTOMapper;
import com.licenta.service.dto.UserDTO;
import com.licenta.service.dto.UserDTOMapper;
import com.licenta.service.exception.UserAlreadyExistsException;
import com.licenta.service.exception.UserNotFoundException;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService{

    private final UserJPARepository userJPARepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final UserVOMapper userVOMapper;
    private final LoggedInUserDTOMapper loggedInUserDTOMapper;
    private final ProfilePictureService profilePictureService;
    private final QRCodeGeneratorService qrCodeGeneratorService;
    @PersistenceContext
    private EntityManager entityManager;

    public UserServiceImpl(UserJPARepository userJPARepository, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, ConfirmationTokenService confirmationTokenService, EmailSender emailSender, UserVOMapper userVOMapper, LoggedInUserDTOMapper loggedInUserDTOMapper, ProfilePictureService profilePictureService, QRCodeGeneratorService qrCodeGeneratorService) {
        this.userJPARepository = userJPARepository;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSender = emailSender;
        this.userVOMapper = userVOMapper;
        this.loggedInUserDTOMapper = loggedInUserDTOMapper;
        this.profilePictureService = profilePictureService;
        this.qrCodeGeneratorService = qrCodeGeneratorService;
    }

    @Override
    @Transactional
    public UserDTO save(UserDTO userDTO, MultipartFile file) throws IOException, WriterException {
        checkUsersExistence(userDTO);

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userDTOMapper.getEntityFromDTO(userDTO);

        user.setCreatedAt(LocalDateTime.now());
        user.setPoints(100.0);
        user.setEnabled(false);
        userJPARepository.save(user);

        profilePictureService.save(file, user);
        qrCodeGeneratorService.generateAndSaveQRCodeOfUser(user);

        String token = generateConfirmationToken();
        saveConfirmationToken(token, user);

        sendConfirmationEmail(userDTO, token);

        userDTO = userDTOMapper.getDTOFromEntity(user);
        return userDTO;
    }

    @Transactional
    public void checkUsersExistence(UserDTO userDTO) {
        if(isExisting(userDTO)) {
            if(getUserByEmail(userDTO.getEmail()).isEnabled()) {
                throw new UserAlreadyExistsException("An user with this email already exists!");
            }
            else {
                confirmationTokenService.deleteTokenOfUser(userDTO.getEmail());
                qrCodeGeneratorService.deleteQRCodeOfUser(userDTO.getEmail());
                profilePictureService.deleteProfilePictureOfUserIfExists(userDTO.getEmail());
                userJPARepository.delete(getUserByEmail(userDTO.getEmail()));
            }
        }

    }

    private void sendConfirmationEmail(UserDTO userDTO, String token) {
        String link = "http://localhost:8081/api/user/confirm?token=" + token;
        String subject = "Confirmare adresa email";
        emailSender.send(
                userDTO.getEmail(),
                userDTO.getFirstName(),
                link,
                subject);
    }

    private static String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }

    private void saveConfirmationToken(String token, User user) {
        confirmationTokenService.saveConfirmationToken(
                new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        user
                ));
    }

    @Override
    public boolean isExisting(UserDTO userDTO) {
        return userJPARepository.findByEmail(userDTO.getEmail()).isPresent();
    }

    @Override
    public LoggedInUserDTO login(UserDTO userDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getEmail(),
                        userDTO.getPassword()
                )
        );
        User user = getUserByEmail(userDTO.getEmail());
        LoggedInUserDTO loggedInUserDTO = loggedInUserDTOMapper.getDTOFromEntity(user);

        String jwtToken = jwtService.generateJwtToken(user);
        loggedInUserDTO.setToken(jwtToken);
        loggedInUserDTO.setTokenExpirationDate(jwtService.extractClaim(jwtToken, Claims::getExpiration));

        return loggedInUserDTO;
    }

    @Override
    public User getUserByEmail(String email) {
        return userJPARepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void confirmUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        User user = confirmationToken.getUser();
        enableUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userJPARepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public void updateUserPoints(User user, Double amount) {
        user.setPoints(amount);
        userJPARepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getProfileInformation() {
        User user = findById(UserContextHolder.getUserContext().getUserId());
        ProfilePictureVO profilePictureVO = profilePictureService.getVOByUserId(user.getId());
        QRCodeVO qrCodeVO = qrCodeGeneratorService.getVOByUserId(user.getId());
        return userVOMapper.getVOFromEntity(user, profilePictureVO, qrCodeVO);
    }

    @Override
    @Transactional
    public UserDTO editProfileInformation(UserDTO userDTO) {
        User user = getById(UserContextHolder.getUserContext().getUserId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        userJPARepository.save(user);
        return userDTOMapper.getDTOFromEntity(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userJPARepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDTO getDTOFromString(String userDTOString) {
        return userDTOMapper.getDTOFromString(userDTOString);
    }

    @Transactional
    public void enableUser(User user) {
        user.setEnabled(true);
        user.setRole(Role.USER);
        userJPARepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserEmailVO> getFilteredUserEmails(String email) {
        StringBuilder jpqlBuilder = new StringBuilder(
                "SELECT u FROM User u WHERE u.id != :userId AND u.enabled = true AND u.email != 'admin@admin'" +
                        "AND u.id NOT IN (" +
                        "SELECT c.sender.id FROM ChatRoom c WHERE c.recipient.id = :userId " +
                        "OR c.sender.id = :userId OR c.recipient.id = u.id" +
                        ") "
        );

        if (email != null && !email.isEmpty()) {
            jpqlBuilder.append("AND (LOWER(u.email) LIKE :email) ");
        }

        TypedQuery<User> query = entityManager.createQuery(jpqlBuilder.toString(), User.class);
        query.setParameter("userId", UserContextHolder.getUserContext().getUserId());
        if (email != null && !email.isEmpty()) {
            query.setParameter("email", "%" + email.toLowerCase() + "%");
        }
        List<User> resultList = query.getResultList();
        return resultList.stream()
                .map(user -> new UserEmailVO(user.getId(), user.getEmail()))
                .toList();
    }


}
