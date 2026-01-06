package com.testmaker.api.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveAnswerRequest {
    private String answer;
    private Boolean isCorrect;
}
