package com.testmaker.api.dto.exception;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private String date;
    private String time;
}
