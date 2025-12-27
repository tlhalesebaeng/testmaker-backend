package com.testmaker.api.controller;

import com.testmaker.api.dto.auth.*;
import com.testmaker.api.dto.user.UserResponse;
import com.testmaker.api.entity.User;
import com.testmaker.api.mapper.UserMapper;
import com.testmaker.api.service.user.UserServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceInterface userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest requestDto) {
        UserResponse responseDto = UserMapper.toResponse(userService.createUser(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(false, responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest requestDto) {
        UserResponse responseDto = UserMapper.toResponse(userService.login(requestDto));
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(true, responseDto));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<AuthResponse> forgotPassword(@Valid @RequestBody ResetPasswordRequest requestDto) {
        UserResponse responseDto = UserMapper.toResponse(userService.forgotPassword(requestDto));
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(false, responseDto));
    }

    @PostMapping("/verify/code")
    public ResponseEntity<Object> verifyPasswordResetCode(@Valid @RequestBody VerifyCodeRequest requestDto) {
        UserResponse responseDto = UserMapper.toResponse(userService.verifyPasswordResetCode(requestDto));
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(false, responseDto));
    }

    @PostMapping("/verify/email")
    public ResponseEntity<AuthResponse> verifyEmailAddress(@Valid @RequestBody VerifyCodeRequest requestDto) {
        UserResponse responseDto = UserMapper.toResponse(userService.verifyEmailAddress(requestDto));
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(true, responseDto));
    }

    @PatchMapping("/password/new")
    public ResponseEntity<Object> newPassword(@Valid @RequestBody NewPasswordRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> checkAuth(@CookieValue(value = "access_token", required = false) String jwt) {
        User user = userService.checkAuth(jwt);
        if(user == null) return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(false, null));
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(true, UserMapper.toResponse(user)));
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout() {
        userService.logout();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
