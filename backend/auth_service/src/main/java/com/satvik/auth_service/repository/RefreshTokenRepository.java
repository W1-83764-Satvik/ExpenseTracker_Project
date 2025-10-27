package com.satvik.auth_service.repository;

import com.satvik.auth_service.entities.RefreshToken;
import com.satvik.auth_service.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

     Optional<RefreshToken> findByRefreshToken(String refreshToken);
     void deleteByUser(UserInfo name);
}
