package com.satvik.auth_service.dto.authResponse;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MeResponseDto {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
}

