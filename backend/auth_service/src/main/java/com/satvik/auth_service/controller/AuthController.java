package com.satvik.auth_service.controller;

import com.satvik.auth_service.dto.authRequest.LoginRequestDto;
import com.satvik.auth_service.dto.authRequest.UserInfoDto;
import com.satvik.auth_service.dto.authResponse.JwtResponseDto;
import com.satvik.auth_service.dto.authResponse.MeResponseDto;
import com.satvik.auth_service.dto.responseentitydto.ApiResponse;
import com.satvik.auth_service.entities.RefreshToken;
import com.satvik.auth_service.entities.UserInfo;
import com.satvik.auth_service.services.RefreshTokenService;
import com.satvik.auth_service.services.TokenService;
import com.satvik.auth_service.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    // SIGNUP API
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<JwtResponseDto>> signUp(@Valid @RequestBody UserInfoDto userInfoDto) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Received signup request for email: {}", userInfoDto.getEmail());

            // Register new user
            authService.signUpUser(userInfoDto);
            log.info("User registered successfully: {}", userInfoDto.getEmail());

            String email = userInfoDto.getEmail();
            String jwtToken = tokenService.generateToken(email);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);

            log.debug("Generated JWT and RefreshToken for user: {}", email);

            JwtResponseDto jwtResponse = JwtResponseDto.builder()
                    .username(userInfoDto.getUsername())
                    .email(userInfoDto.getEmail())
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

            ApiResponse<JwtResponseDto> response = ApiResponse.<JwtResponseDto>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("User registered successfully")
                    .data(jwtResponse)
                    .timestamp(now)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    // SIGNIN API
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Received login request for email: {}", loginRequestDto.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );

            if (!authentication.isAuthenticated()) {
                log.warn("Authentication failed for email: {}", loginRequestDto.getEmail());
                ApiResponse<JwtResponseDto> invalidResponse = ApiResponse.<JwtResponseDto>builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("Invalid credentials")
                        .timestamp(now)
                        .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(invalidResponse);
            }

            UserInfo user = (UserInfo) authentication.getPrincipal();
            log.info("User authenticated successfully: {}", user.getEmail());

            String jwtToken = tokenService.generateToken(user.getEmail());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
            log.debug("Generated tokens for user: {}", user.getEmail());

            JwtResponseDto jwtResponse = JwtResponseDto.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

            ApiResponse<JwtResponseDto> successResponse = ApiResponse.<JwtResponseDto>builder()
                    .status(HttpStatus.OK.value())
                    .message("Login successful")
                    .data(jwtResponse)
                    .timestamp(now)
                    .build();

            log.info("Login successful for user: {}", user.getEmail());
            return ResponseEntity.ok(successResponse);
    }

    // LOGOUT API
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody String refreshToken) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Received logout request with refresh token: {}", refreshToken);

            authService.logout(refreshToken);
            log.info("Logout successful for refresh token: {}", refreshToken);

            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .status(HttpStatus.OK.value())
                            .message("Logout successful")
                            .data("User logged out and refresh token deleted")
                            .timestamp(LocalDateTime.now())
                            .build()
            );

    }

    // GET CURRENT AUTHENTICATED USER
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponseDto>> getCurrentUser(Authentication authentication) {
        LocalDateTime now = LocalDateTime.now();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated access to /auth/v1/me");
            ApiResponse<MeResponseDto> response = ApiResponse.<MeResponseDto>builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("User is not authenticated")
                    .timestamp(now)
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // principal comes from SecurityContext (set by your JWT filter)
        UserInfo user = (UserInfo) authentication.getPrincipal();

        log.info("Fetching /me for user: {}", user.getEmail());

        MeResponseDto meResponse = MeResponseDto.builder()
                .id(user.getUserId())
                .username(user.getSubject())
                .email(user.getUsername())
                .roles(
                        user.getAuthorities()
                                .stream()
                                .map(grantedAuthority -> grantedAuthority.getAuthority())
                                .collect(java.util.stream.Collectors.toSet())
                )
                .build();

        ApiResponse<MeResponseDto> response = ApiResponse.<MeResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Current user details fetched successfully")
                .data(meResponse)
                .timestamp(now)
                .build();

        return ResponseEntity.ok(response);
    }

}
