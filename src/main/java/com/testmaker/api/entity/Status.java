package com.testmaker.api.entity;

import com.testmaker.api.utils.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Entity
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "status")
    private Collection<User> user;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus name;

    public Status(AccountStatus status) {
        this.name = status;
    }
}
