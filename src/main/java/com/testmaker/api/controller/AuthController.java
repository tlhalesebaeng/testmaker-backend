package com.testmaker.api.controller;

import com.testmaker.api.dto.auth.*;
import com.testmaker.api.dto.user.UserResponse;
import com.testmaker.api.mapper.UserMapper;
import com.testmaker.api.service.user.UserServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceInterface userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupRequest requestDto) {
        UserResponse responseDto = UserMapper.toResponse(userService.createUser(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/code")
    public ResponseEntity<Object> confirmCode(@Valid @RequestBody ConfirmCodeRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/verify/email")
    public ResponseEntity<Object> verifyEmailAddress(@Valid @RequestBody ConfirmCodeRequest requestDto) {
        UserResponse responseDto = UserMapper.toResponse(userService.verifyEmailAddress(requestDto));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/password/new")
    public ResponseEntity<Object> newPassword(@Valid @RequestBody NewPasswordRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
