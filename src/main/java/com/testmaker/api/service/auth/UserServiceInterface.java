package com.testmaker.api.service.auth;

import com.testmaker.api.dto.auth.*;
import com.testmaker.api.entity.User;

public interface UserServiceInterface {
    User createUser(SignupRequest requestDto);
    User verifyEmailAddress(VerifyCodeRequest requestDto);
    User forgotPassword(ForgotPasswordRequest requestDto);
    User verifyPasswordResetCode(VerifyCodeRequest requestDto);
    User resetPassword(ResetPasswordRequest requestDto, Integer code);
    User checkAuth(String jwt);
    User login(LoginRequest requestDto);
    void logout();

}
