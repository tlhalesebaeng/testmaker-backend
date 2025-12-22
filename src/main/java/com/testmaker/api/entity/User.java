package com.testmaker.api.entity;

import com.testmaker.api.utils.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String username;
    private String email;
    private String password;
    private Integer emailVerificationCode;
    private LocalDateTime emailVerificationCodeExpiration;
}
