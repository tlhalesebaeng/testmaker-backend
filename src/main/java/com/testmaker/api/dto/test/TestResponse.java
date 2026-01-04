package com.testmaker.api.dto.test;

import com.testmaker.api.dto.question.QuestionResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class TestResponse {
    private Long id;
    private Long creator;
    private String title;
    private Integer duration;
    private Collection<QuestionResponse> questions;
}
