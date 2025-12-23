package com.testmaker.api.controller;

import com.testmaker.api.dto.auth.*;
import com.testmaker.api.dto.user.UserResponse;
import com.testmaker.api.entity.User;
import com.testmaker.api.mapper.UserMapper;
import com.testmaker.api.service.cookie.CookieServiceInterface;
import com.testmaker.api.service.jwt.JwtServiceInterface;
import com.testmaker.api.service.user.UserServiceInterface;
import jakarta.servlet.http.HttpServletResponse;
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
    private final CookieServiceInterface cookieService;
    private final JwtServiceInterface jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest requestDto) {
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
    public ResponseEntity<UserResponse> verifyEmailAddress(@Valid @RequestBody ConfirmCodeRequest requestDto, HttpServletResponse response) {
        User user = userService.verifyEmailAddress(requestDto);
        String token = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_token", token));
        UserResponse responseDto = UserMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/password/new")
    public ResponseEntity<Object> newPassword(@Valid @RequestBody NewPasswordRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        response.addCookie(cookieService.create("access_token", ""));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
