package com.testmaker.api.mapper;

import com.testmaker.api.dto.question.QuestionResponse;
import com.testmaker.api.entity.Question;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class QuestionMapper {
    public static QuestionResponse toResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setQuestion(question.getQuestion());
        response.setType(question.getType().name());
        response.setAnswers(AnswerMapper.toListResponse(question.getAnswers()));
        return response;
    }

    public static Collection<QuestionResponse> toListResponse(Collection<Question> questions) {
        Set<QuestionResponse> response = new HashSet<>();
        for(Question question : questions) response.add(QuestionMapper.toResponse(question));
        return response;
    }
}
