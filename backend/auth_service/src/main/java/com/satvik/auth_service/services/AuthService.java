package com.satvik.auth_service.services;

import com.satvik.auth_service.dto.authRequest.UserInfoDto;
import com.satvik.auth_service.entities.RefreshToken;
import com.satvik.auth_service.entities.UserInfo;
import com.satvik.auth_service.enums.Roles;
import com.satvik.auth_service.exception.exception_classes.InvalidTokenException;
import com.satvik.auth_service.exception.exception_classes.UserAlreadyExistException;
import com.satvik.auth_service.repository.RefreshTokenRepository;
import com.satvik.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public void signUpUser(UserInfoDto userInfoDto) {
        String email = userInfoDto.getEmail();
        log.info("Attempting to register new user with email: {}", email);

        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Registration failed — email already exists: {}", email);
            throw new UserAlreadyExistException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(userInfoDto.getPassword());
        log.debug("Password encoded successfully for email: {}", email);

        UserInfo user = UserInfo.builder()
                .username(userInfoDto.getUsername())
                .password(encodedPassword)
                .email(email)
                .role(Roles.USER)
                .active(true)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", email);
    }


    @Transactional
    public void logout(String refreshToken) {

        String tokenPrefix = refreshToken.length() > 10 ? refreshToken.substring(0, 10) + "..." : refreshToken;

        log.info("Logout attempt for refresh token prefix: {}", tokenPrefix);

        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Invalid refresh token provided during logout. Prefix: {}", tokenPrefix);
                    return new InvalidTokenException("Invalid refresh token");
                });


        if (token.getExpiration() != null && token.getExpiration().isBefore(Instant.now())) {
            log.warn("Expired refresh token detected during logout. Prefix: {}", tokenPrefix);
            refreshTokenRepository.delete(token);
            throw new InvalidTokenException("Refresh token expired");
        }


        refreshTokenRepository.delete(token);
        log.info("Logout successful — refresh token invalidated. Prefix: {}", tokenPrefix);
    }
}
