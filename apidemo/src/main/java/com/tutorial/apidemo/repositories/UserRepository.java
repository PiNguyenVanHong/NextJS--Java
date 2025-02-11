package com.tutorial.apidemo.repositories;

import com.tutorial.apidemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationCode);
}
