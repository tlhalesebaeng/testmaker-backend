package com.testmaker.api.controller;

import com.testmaker.api.dto.auth.LoginRequest;
import com.testmaker.api.dto.auth.NewPasswordRequest;
import com.testmaker.api.dto.auth.ResetPasswordRequest;
import com.testmaker.api.dto.auth.SignupRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupRequest requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/password/new")
    public ResponseEntity<Object> newPassword(@Valid @RequestBody NewPasswordRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
