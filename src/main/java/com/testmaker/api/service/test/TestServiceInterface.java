package com.testmaker.api.service.test;

import com.testmaker.api.dto.test.CreateTestRequest;
import com.testmaker.api.dto.test.SaveTestRequest;
import com.testmaker.api.entity.Test;

public interface TestServiceInterface {
    Test createTest(CreateTestRequest requestDto);
    Test saveNewTestProgress(SaveTestRequest requestDto);
}
