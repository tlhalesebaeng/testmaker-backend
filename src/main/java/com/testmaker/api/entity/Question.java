package com.testmaker.api.entity;

import com.testmaker.api.utils.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Answer> answers;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private QuestionType type;

    @Column(nullable = false)
    private String question;
}
