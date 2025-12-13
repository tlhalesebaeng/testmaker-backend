package com.testmaker.api.dto.exception;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ExceptionResponse {
    private String message;
    private String date;
    private String time;

    public ExceptionResponse(String message) {
        this.message = message;
        this.date = LocalDate.now().toString();
        this.time = LocalTime.now().toString();
    }
}
