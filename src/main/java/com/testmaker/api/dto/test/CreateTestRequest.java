package com.testmaker.api.dto.test;

import com.testmaker.api.dto.question.QuestionRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateTestRequest {
    @NotBlank(message = "Test title required! Please provide a test title")
    private String title;

    @NotNull(message = "Test questions required! Please provide test questions")
    private List<QuestionRequest> questions;

    private Integer duration;
}
