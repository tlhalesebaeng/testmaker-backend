package com.testmaker.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private User user;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Question> questions;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String title;
    private LocalDateTime createdAt;

    @PrePersist
    private void beforePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
