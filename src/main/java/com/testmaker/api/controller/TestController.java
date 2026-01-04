package com.testmaker.api.controller;

import com.testmaker.api.dto.test.CreateTestRequest;
import com.testmaker.api.dto.test.TestResponse;
import com.testmaker.api.mapper.TestMapper;
import com.testmaker.api.service.test.TestServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/test")
public class TestController {
    private final TestServiceInterface testService;

    @PostMapping("")
    public ResponseEntity<TestResponse> createTest(@Valid @RequestBody CreateTestRequest requestDto) {
        TestResponse response = TestMapper.toResponse(testService.createTest(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
