package com.licenta.service;

import com.google.zxing.WriterException;
import com.licenta.config.service.JwtService;
import com.licenta.context.UserContextHolder;
import com.licenta.domain.ConfirmationToken;
import com.licenta.domain.Role;
import com.licenta.domain.User;
import com.licenta.domain.repository.UserJPARepository;
import com.licenta.domain.vo.ProfilePictureVO;
import com.licenta.domain.vo.QRCodeVO;
import com.licenta.domain.vo.UserVO;
import com.licenta.domain.vo.UserVOMapper;
import com.licenta.email.EmailSender;
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

import java.io.IOException;
import java.time.LocalDateTime;
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
        if(isExisting(userDTO))
            throw new UserAlreadyExistsException("An user with this email already exists!");

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

    private void sendConfirmationEmail(UserDTO userDTO, String token) {
        String link = "http://localhost:8081/api/user/confirm?token=" + token;
        String subject = "Confirmare adresa email";
        emailSender.send(
                userDTO.getEmail(),
                buildEmail(userDTO.getFirstName(), link),
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
        user.setPoints(user.getPoints() + amount);
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
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
