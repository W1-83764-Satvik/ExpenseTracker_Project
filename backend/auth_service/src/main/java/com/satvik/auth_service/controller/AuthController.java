package com.satvik.auth_service.controller;

import com.satvik.auth_service.dto.authRequest.LoginRequestDto;
import com.satvik.auth_service.dto.authRequest.UserInfoDto;
import com.satvik.auth_service.dto.authResponse.JwtResponseDto;
import com.satvik.auth_service.dto.responseentitydto.ApiResponse;
import com.satvik.auth_service.entities.RefreshToken;
import com.satvik.auth_service.entities.UserInfo;
import com.satvik.auth_service.exceptions.InvalidTokenException;
import com.satvik.auth_service.exceptions.UserAlreadyExistsException;
import com.satvik.auth_service.services.RefreshTokenService;
import com.satvik.auth_service.services.TokenService;
import com.satvik.auth_service.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // SIGNUP API
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<JwtResponseDto>> signUp(@Valid @RequestBody UserInfoDto userInfoDto) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Received signup request for email: {}", userInfoDto.getEmail());

        try {
            // Register new user
            authService.signUpUser(userInfoDto);
            log.info("User registered successfully: {}", userInfoDto.getEmail());

            String email = userInfoDto.getEmail();
            String jwtToken = tokenService.generateToken(email);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);

            log.debug("Generated JWT and RefreshToken for user: {}", email);

            JwtResponseDto jwtResponse = JwtResponseDto.builder()
                    .username(userInfoDto.getUsername())
                    .token(jwtToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

            ApiResponse<JwtResponseDto> response = ApiResponse.<JwtResponseDto>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("User registered successfully")
                    .data(jwtResponse)
                    .timestamp(now)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UserAlreadyExistsException e) {
            log.warn("Signup failed — user already exists: {}", userInfoDto.getEmail());
            ApiResponse<JwtResponseDto> errorResponse = ApiResponse.<JwtResponseDto>builder()
                    .status(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .timestamp(now)
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

        } catch (Exception e) {
            log.error("Signup failed for email: {}", userInfoDto.getEmail(), e);
            ApiResponse<JwtResponseDto> errorResponse = ApiResponse.<JwtResponseDto>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error during registration")
                    .timestamp(now)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // SIGNIN API
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Received login request for email: {}", loginRequestDto.getEmail());

        try {
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
                    .token(jwtToken)
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

        } catch (BadCredentialsException e) {
            log.warn("Invalid login attempt for email: {}", loginRequestDto.getEmail());
            ApiResponse<JwtResponseDto> errorResponse = ApiResponse.<JwtResponseDto>builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid email or password")
                    .timestamp(now)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            log.error("Unexpected error during login for email: {}", loginRequestDto.getEmail(), e);
            ApiResponse<JwtResponseDto> errorResponse = ApiResponse.<JwtResponseDto>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error during login")
                    .timestamp(now)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // LOGOUT API
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody String refreshToken) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Received logout request with refresh token: {}", refreshToken);

        try {
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

        } catch (InvalidTokenException e) {
            log.warn("Invalid or expired refresh token during logout: {}", refreshToken);
            ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .data(e.getMessage())
                    .timestamp(now)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);

        } catch (Exception e) {
            log.error("Unexpected error during logout for refresh token: {}", refreshToken, e);
            ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error during logout")
                    .timestamp(now)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
