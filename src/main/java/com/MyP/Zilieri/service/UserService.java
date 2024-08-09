package com.MyP.Zilieri.service;

import com.MyP.Zilieri.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    User save(User user);

    List<User> findAll();

    void deleteById(Long id);

    Optional<User> findByEmail(String email);


    Optional<User> findByResetToken(String resetToken);


    UserDetailsService userDetailsService();

    User findOrCreateUser(String email, String username, Boolean isExternalAuth);

    Optional<User> findByActivationToken(String token);
}
