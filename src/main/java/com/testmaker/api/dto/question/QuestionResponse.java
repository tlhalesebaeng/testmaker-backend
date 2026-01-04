package com.testmaker.api.dto.question;

import com.testmaker.api.dto.answer.AnswerResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class QuestionResponse {
    private Long id;
    private String type;
    private String question;
    private Collection<AnswerResponse> answers;
}
