package com.testmaker.api.controller;

import com.testmaker.api.dto.test.CreateTestRequest;
import com.testmaker.api.dto.test.SaveTestRequest;
import com.testmaker.api.dto.test.TestResponse;
import com.testmaker.api.entity.Test;
import com.testmaker.api.mapper.TestMapper;
import com.testmaker.api.service.test.TestServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<TestResponse> getTest(@PathVariable Long id) {
        TestResponse response = TestMapper.toResponse(testService.getTestById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 'My' refers to the principal user i.e. The currently authenticated user
    @GetMapping("/mine")
    public ResponseEntity<Collection<TestResponse>> getAllMyTests() {
        Collection<TestResponse> response = TestMapper.toListResponse(testService.getAllMyTests());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/save-progress")
    public ResponseEntity<TestResponse> saveNewTestProgress(@Valid @RequestBody SaveTestRequest requestDto) {
        TestResponse response = TestMapper.toResponse(testService.saveNewTestProgress(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/save-progress/{id}")
    public ResponseEntity<TestResponse> saveExistingTestProgress(@Valid @RequestBody SaveTestRequest requestDto, @PathVariable Long id) {
        TestResponse response = TestMapper.toResponse(testService.saveExistingTestProgress(requestDto, id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
