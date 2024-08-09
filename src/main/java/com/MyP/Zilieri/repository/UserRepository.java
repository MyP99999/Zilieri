package com.MyP.Zilieri.repository;

import com.MyP.Zilieri.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByActivationToken(String activationToken);
    Optional<User> findByResetToken(String resetToken);

}
