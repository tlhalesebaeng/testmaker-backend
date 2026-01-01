package com.testmaker.api.repository;

import com.testmaker.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailVerificationCode(Integer code);
    Optional<User> findByPasswordResetCode(Integer code);
}
