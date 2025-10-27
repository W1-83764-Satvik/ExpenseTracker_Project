package com.satvik.auth_service.services;

import com.satvik.auth_service.entities.UserInfo;
import com.satvik.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserInfo> findByEmail(String username){
        return userRepository.findByEmail(username);
    }
}
