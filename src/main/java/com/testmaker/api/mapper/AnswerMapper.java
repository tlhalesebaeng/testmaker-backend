package com.testmaker.api.mapper;

import com.testmaker.api.dto.answer.AnswerResponse;
import com.testmaker.api.entity.Answer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AnswerMapper {
    public static AnswerResponse toResponse(Answer answer) {
        AnswerResponse response = new AnswerResponse();
        response.setId(answer.getId());
        response.setAnswer(answer.getAnswer());
        response.setIsCorrect(answer.getIsCorrect());
        return response;
    }

    public static Collection<AnswerResponse> toListResponse(Collection<Answer> answers) {
        Set<AnswerResponse> response = new HashSet<>();
        for(Answer answer : answers) response.add(AnswerMapper.toResponse(answer));
        return response;
    }
}
