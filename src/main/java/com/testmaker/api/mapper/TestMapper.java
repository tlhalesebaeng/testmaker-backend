package com.testmaker.api.mapper;

import com.testmaker.api.dto.test.TestResponse;
import com.testmaker.api.entity.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestMapper {
    public static TestResponse toResponse(Test test) {
        TestResponse response = new TestResponse();
        response.setId(test.getId());
        response.setTitle(test.getTitle());
        response.setDuration(test.getDuration());
        response.setCreator(test.getUser().getId());
        response.setQuestions(QuestionMapper.toListResponse(test.getQuestions()));
        return response;
    }

    public static Collection<TestResponse> toListResponse(Collection<Test> tests) {
        Set<TestResponse> response = new HashSet<>();
        for(Test test : tests) response.add(TestMapper.toResponse(test));
        return response;
    }
}
