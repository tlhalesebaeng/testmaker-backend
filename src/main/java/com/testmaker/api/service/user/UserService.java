package com.testmaker.api.service.user;

import com.testmaker.api.dto.auth.ConfirmCodeRequest;
import com.testmaker.api.dto.auth.SignupRequest;
import com.testmaker.api.entity.User;
import com.testmaker.api.exception.InvalidVerificationCodeException;
import com.testmaker.api.repository.UserRepository;
import com.testmaker.api.utils.Code;
import com.testmaker.api.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface{
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${api.email-verification-code.expiration}")
    private Integer emailVerificationCodeExpiration;

    @Override
    public User createUser(SignupRequest requestDto) {
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setStatus(Status.PENDING_EMAIL_VERIFICATION);
        // TODO: Send the user an email with the 6 digit verification code
        user.setEmailVerificationCode(Code.generate());
        user.setEmailVerificationCodeExpiration(LocalDateTime.now().plusMinutes(emailVerificationCodeExpiration));
        return userRepo.save(user);
    }

    @Override
    public User verifyEmailAddress(ConfirmCodeRequest requestDto) {
        Optional<User> optionalUser = userRepo.findByEmailVerificationCodeAndStatus(requestDto.getCode(), Status.PENDING_EMAIL_VERIFICATION);
        User user = optionalUser.orElseThrow(() -> new InvalidVerificationCodeException("Invalid code provided! Please check your code and try again"));
        user.setStatus(Status.ACTIVE);
        return userRepo.save(user);
    }
}
