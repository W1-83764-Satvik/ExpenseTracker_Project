package com.satvik.auth_service.repository;

import com.satvik.auth_service.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
     Optional<UserInfo> findByUsername(String username);
     boolean existsByUsername(String username);
     Optional<UserInfo> findByEmail(String email);
}
