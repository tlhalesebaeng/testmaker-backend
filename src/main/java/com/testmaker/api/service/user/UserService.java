package com.testmaker.api.service.user;

import com.testmaker.api.dto.auth.SignupRequest;
import com.testmaker.api.entity.User;
import com.testmaker.api.repository.UserRepository;
import com.testmaker.api.utils.Code;
import com.testmaker.api.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface{
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User createUser(SignupRequest requestDto) {
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setStatus(Status.PENDING_EMAIL_VERIFICATION);
        // TODO: Send the user an email with the 6 digit verification code
        user.setEmailVerificationCode(Code.generate());
        return userRepo.save(user);
    }
}
