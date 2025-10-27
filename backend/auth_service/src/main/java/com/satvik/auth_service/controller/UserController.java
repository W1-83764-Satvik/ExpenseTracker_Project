package com.satvik.auth_service.controller;

import com.satvik.auth_service.dto.responseentitydto.ApiResponse;
import com.satvik.auth_service.entities.UserInfo;
import com.satvik.auth_service.services.TokenService;
import com.satvik.auth_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt/v1")
public class UserController {

    private final TokenService tokenService;
    private final UserService userService;

    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse<String>> validateToken(@RequestParam String token) {
        try {

            String username = tokenService.extractName(token);


            UserDetails userDetails = userService.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            boolean isValid = tokenService.isTokenValid(token, userDetails);


            if (isValid) {
                ApiResponse<String> response = ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Token is valid")
                        .data(username)
                        .timestamp(LocalDateTime.now())
                        .build();

                return ResponseEntity.ok(response);
            } else {
                ApiResponse<String> response = ApiResponse.<String>builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("Invalid or expired token")
                        .timestamp(LocalDateTime.now())
                        .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Token validation failed: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
