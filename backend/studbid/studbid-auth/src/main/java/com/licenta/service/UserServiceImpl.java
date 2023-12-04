package com.licenta.service;

import com.licenta.config.service.JwtService;
import com.licenta.domain.User;
import com.licenta.domain.repository.UserJPARepository;
import com.licenta.service.dto.UserDTO;
import com.licenta.service.dto.UserDTOMapper;
import com.licenta.service.exception.UserAlreadyExists;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService{

    private final UserJPARepository userJPARepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserJPARepository userJPARepository, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userJPARepository = userJPARepository;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public UserDTO save(UserDTO userDTO) {
        if(isExisting(userDTO))
            throw new UserAlreadyExists("An user with this email already exists!");

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userDTOMapper.getEntityFromDTO(userDTO);

        user.setPoints(100.0);
        userJPARepository.save(user);
        userDTO = userDTOMapper.getDTOFromEntity(user);
        userDTO.setAuthenticationResponse(new AuthenticationResponse(jwtService.generateJwtToken(user)));

        return userDTO;
    }

    public boolean isExisting(UserDTO userDTO) {
        return userJPARepository.findByEmail(userDTO.getEmail()).isPresent();
    }

    @Override
    public AuthenticationResponse login(UserDTO userDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getEmail(),
                        userDTO.getPassword()
                )
        );
        User user = getUserByEmail(userDTO.getEmail());
        return new AuthenticationResponse(jwtService.generateJwtToken(user));
    }

    public User getUserByEmail(String email) {
        return userJPARepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
