package com.satvik.auth_service.services;

import com.satvik.auth_service.entities.RefreshToken;
import com.satvik.auth_service.entities.UserInfo;
import com.satvik.auth_service.exception.exception_classes.UserNotFoundException;
import com.satvik.auth_service.repository.RefreshTokenRepository;
import com.satvik.auth_service.repository.UserRepository;
import com.satvik.auth_service.security.JwtConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;

    @Transactional
    public RefreshToken createRefreshToken(String email) {
        UserInfo user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiration(Instant.now().plusMillis(jwtConfig.getRefreshTokenExpiry().toMillis()))
                .refreshToken(UUID.randomUUID().toString())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }



    public Optional<RefreshToken> findByRefreshToken(String refreshtoken){
        return refreshTokenRepository.findByRefreshToken(refreshtoken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiration().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please sign in again.");
        }
        return token;
    }

    public boolean isTokenValid(RefreshToken token) {
        return !token.getExpiration().isBefore(Instant.now());
    }

}
