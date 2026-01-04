package com.testmaker.api.dto.answer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerResponse {
    private Long id;
    private String answer;
    private Boolean isCorrect;
}
