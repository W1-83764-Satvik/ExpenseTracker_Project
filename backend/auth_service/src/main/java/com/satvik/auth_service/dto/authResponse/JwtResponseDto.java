package com.satvik.auth_service.dto.authResponse;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDto {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Access token cannot be blank")
    private String token;

    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;
}
