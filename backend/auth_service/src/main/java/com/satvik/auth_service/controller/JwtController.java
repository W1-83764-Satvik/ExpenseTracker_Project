package com.satvik.auth_service.controller;

import com.satvik.auth_service.dto.authResponse.JwtResponseDto;
import com.satvik.auth_service.dto.jwtRequest.RefreshTokenRequestDto;
import com.satvik.auth_service.dto.responseentitydto.ApiResponse;
import com.satvik.auth_service.entities.RefreshToken;
import com.satvik.auth_service.services.RefreshTokenService;
import com.satvik.auth_service.services.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt/v1")
public class JwtController {

    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponseDto>> refresh(
            @Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {

        String refreshTokenStr = refreshTokenRequestDto.getRefreshToken();
        log.info("Received refresh token request");

        return refreshTokenService.findByRefreshToken(refreshTokenStr)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    log.info("Issuing new access token for user: {}", user.getEmail());
                    String newToken = tokenService.generateToken(user.getEmail());

                    JwtResponseDto jwtResponse = JwtResponseDto.builder()
                            .username(user.getUsername())
                            .accessToken(newToken)
                            .refreshToken(refreshTokenStr)
                            .build();

                    ApiResponse<JwtResponseDto> response = ApiResponse.<JwtResponseDto>builder()
                            .status(HttpStatus.OK.value())
                            .message("Access token refreshed successfully")
                            .data(jwtResponse)
                            .timestamp(LocalDateTime.now())
                            .build();

                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    log.warn("Invalid refresh token used: {}", refreshTokenStr);
                    ApiResponse<JwtResponseDto> response = ApiResponse.<JwtResponseDto>builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("Invalid or expired refresh token")
                            .timestamp(LocalDateTime.now())
                            .build();
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                });
    }
}
