package com.testmaker.api.dto.answer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerRequest {
    @NotBlank(message = "Answer required! Please provide a valid answer")
    private String answer;
    private Boolean isCorrect;
}
