package com.testmaker.api.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EmailVerificationCode {
    private Long id;
    private Integer code;
    private Integer expiration;
}
