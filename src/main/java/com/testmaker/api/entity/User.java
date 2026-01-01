package com.testmaker.api.entity;

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

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @OneToOne
    @JoinColumn(name = "code_id", referencedColumnName = "id")
    private EmailVerificationCode verificationCode;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;
    private Integer passwordResetCode;
    private Integer emailVerificationCode;
    private LocalDateTime passwordResetCodeExpiration;
    private LocalDateTime emailVerificationCodeExpiration;
}
