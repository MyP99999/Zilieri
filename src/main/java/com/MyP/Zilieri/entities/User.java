package com.MyP.Zilieri.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String email;

    private String password;

    private String lastName;

    private String firstName;

    private String phoneNumber;

    private String address;

    private String profilePicture;

    private Role role;

    @Column(name = "is_external_auth")
    private boolean isExternalAuth;

    private String activationToken;

    private boolean isActive = false;

    @Column(name = "reset_token")
    private String resetToken;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public boolean isExternalAuth() {
        return isExternalAuth;
    }

    public void setExternalAuth(boolean externalAuth) {
        isExternalAuth = externalAuth;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
