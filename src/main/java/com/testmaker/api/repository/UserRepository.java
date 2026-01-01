package com.testmaker.api.repository;

import com.testmaker.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.emailVerificationCode.code = :code")
    Optional<User> findByEmailVerificationCode(Integer code);

    @Query("SELECT u FROM User u WHERE u.resetPasswordCode.code = :code")
    Optional<User> findByPasswordResetCode(Integer code);
}
