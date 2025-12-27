package com.testmaker.api.service.user;

import com.testmaker.api.dto.auth.VerifyCodeRequest;
import com.testmaker.api.dto.auth.LoginRequest;
import com.testmaker.api.dto.auth.ResetPasswordRequest;
import com.testmaker.api.dto.auth.SignupRequest;
import com.testmaker.api.entity.User;

public interface UserServiceInterface {
    User createUser(SignupRequest requestDto);
    User verifyEmailAddress(VerifyCodeRequest requestDto);
    User forgotPassword(ResetPasswordRequest requestDto);
    User verifyPasswordResetCode(VerifyCodeRequest requestDto);
    User checkAuth(String jwt);
    User login(LoginRequest requestDto);
    void logout();
}
