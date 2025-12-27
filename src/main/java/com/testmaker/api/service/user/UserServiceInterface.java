package com.testmaker.api.service.user;

import com.testmaker.api.dto.auth.ConfirmCodeRequest;
import com.testmaker.api.dto.auth.LoginRequest;
import com.testmaker.api.dto.auth.ResetPasswordRequest;
import com.testmaker.api.dto.auth.SignupRequest;
import com.testmaker.api.entity.User;
import jakarta.validation.Valid;

public interface UserServiceInterface {
    User createUser(SignupRequest requestDto);
    User verifyEmailAddress(ConfirmCodeRequest requestDto);
    User resetPassword(ResetPasswordRequest requestDto);
    User verifyPasswordResetCode(ConfirmCodeRequest requestDto);
    User checkAuth(String jwt);
    User login(LoginRequest requestDto);
    void logout();
}
