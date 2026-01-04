package com.testmaker.api.dto.question;

import com.testmaker.api.dto.answer.AnswerRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionRequest {
    @NotBlank(message = "Question required! Please provide a valid question")
    private String question;

    @Valid
    @Size(min = 1, message = "A question should have at least one answer")
    @NotNull(message = "Question answers required! Please provide question answers")
    private List<AnswerRequest> answers;

    @NotBlank(message = "Question type required! Please provide a question type")
    private String type;
}
