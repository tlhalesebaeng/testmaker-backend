package com.testmaker.api.controller;

import com.testmaker.api.dto.auth.CreateAccountRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody CreateAccountRequest requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
