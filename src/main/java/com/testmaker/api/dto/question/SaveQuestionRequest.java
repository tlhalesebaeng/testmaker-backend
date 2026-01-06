package com.testmaker.api.dto.question;

import com.testmaker.api.dto.answer.SaveAnswerRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SaveQuestionRequest {
    private String type;
    private String question;
    private List<SaveAnswerRequest> answers;
}
