package com.testmaker.api.dto.question;

import com.testmaker.api.dto.answer.AnswerRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionRequest {
    @NotBlank(message = "Question required! Please provide a valid question")
    private String question;

    @NotNull(message = "Question answers required! Please provide question answers")
    private List<AnswerRequest> answers;

    private String type;
}
