package com.testmaker.api.dto.test;

import com.testmaker.api.dto.question.SaveQuestionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SaveTestRequest {
    private String title;
    private Integer duration;
    private List<SaveQuestionRequest> questions;
}
