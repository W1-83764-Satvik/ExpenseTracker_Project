package com.satvik.auth_service.services;

import com.satvik.auth_service.entities.UserInfo;
import com.satvik.auth_service.repository.UserRepository;
import com.satvik.auth_service.security.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            throw e;
        }
    }

    public String extractName(String token) {
        return extractPayload(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractPayload(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractName(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        UserInfo userInfo = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        claims.put("roles", userInfo.getRole().name());
        return createToken(claims, username, jwtConfig.getAccessTokenExpiry());
    }

    private String createToken(Map<String, Object> claims, String username, java.time.Duration duration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(duration)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return createToken(Collections.emptyMap(), username, jwtConfig.getRefreshTokenExpiry());
    }
}
