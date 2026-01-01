package com.testmaker.api.repository;

import com.testmaker.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailVerificationCode(Integer code);

    @Query("SELECT u From User u WHERE u.passwordResetCode = :code AND u.passwordResetCodeExpiration > :date")
    Optional<User> findByValidPasswordResetCode(Integer code, LocalDateTime date);
}
