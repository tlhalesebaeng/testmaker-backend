package com.testmaker.api.entity;

import com.testmaker.api.utils.Code;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ResetPasswordCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "resetPasswordCode")
    private User user;

    private Integer code;
    private LocalDateTime expiration;

    public ResetPasswordCode(Integer expiration) {
        code = Code.generate();
        this.expiration = LocalDateTime.now().plusMinutes(expiration);
    }

    public boolean isExpired() {
        return expiration.isBefore(LocalDateTime.now());
    }
}
