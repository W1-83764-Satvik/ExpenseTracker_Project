package com.satvik.auth_service.entities;

import com.satvik.auth_service.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 255)
    private String email; // Used as login identifier

    @Column(length = 100, unique = true)
    private String username; // Optional display name

    @Column(nullable = false)
    private String password; // Hashed password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role; // USER or ADMIN

    @Column(nullable = false)
    private boolean active = true; // Can deactivate account without deleting

    // Assign default role automatically if not set
    @PrePersist
    public void assignDefaultRole() {
        if (role == null) {
            role = Roles.USER;
        }
    }

    // Spring Security expects these methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Return email instead of username (email is your login identifier)
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
