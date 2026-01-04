package com.testmaker.api.dto.test;

import com.testmaker.api.dto.question.QuestionRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateTestRequest {
    @NotBlank(message = "Test title required! Please provide a test title")
    private String title;

    @Valid
    @Size(min = 1, message = "A test should have at least one question")
    @NotNull(message = "Test questions required! Please provide test questions")
    private List<QuestionRequest> questions;

    @NotNull(message = "Test duration required! Please provide a valid test duration")
    private Integer duration;
}
