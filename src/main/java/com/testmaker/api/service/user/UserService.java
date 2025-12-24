package com.testmaker.api.service.user;

import com.testmaker.api.dto.auth.ConfirmCodeRequest;
import com.testmaker.api.dto.auth.LoginRequest;
import com.testmaker.api.dto.auth.SignupRequest;
import com.testmaker.api.entity.User;
import com.testmaker.api.exception.EmailNotVerifiedException;
import com.testmaker.api.exception.InvalidVerificationCodeException;
import com.testmaker.api.repository.UserRepository;
import com.testmaker.api.service.jwt.JwtServiceInterface;
import com.testmaker.api.utils.Code;
import com.testmaker.api.utils.Status;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface{
    private final UserRepository userRepo;
    private final JwtServiceInterface jwtService;
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
        Optional<User> optionalUser = userRepo.findByValidEmailVerificationCode(requestDto.getCode(), LocalDateTime.now(), Status.PENDING_EMAIL_VERIFICATION);
        User user = optionalUser.orElseThrow(() -> new InvalidVerificationCodeException("Invalid code provided! Please check your code and try again"));
        user.setStatus(Status.ACTIVE);
        return userRepo.save(user);
    }

    @Override
    public User login(LoginRequest requestDto) {
        Optional<User> optionalUser = userRepo.findByUsername(requestDto.getUsername());
        User user = optionalUser.orElseThrow(() -> new BadCredentialsException("Incorrect credentials! Please check your credentials and try again"));

        if(user.getStatus().equals(Status.PENDING_EMAIL_VERIFICATION)) {
            user.setEmailVerificationCode(Code.generate());
            user.setEmailVerificationCodeExpiration(LocalDateTime.now().plusMinutes(emailVerificationCodeExpiration));
            userRepo.save(user);
            throw new EmailNotVerifiedException("Email not verified! Please verify your email address");
        }

        if(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            return user;
        } else {
            throw new BadCredentialsException("Incorrect credentials! Please check your credentials and try again");
        }
    }

    @Override
    public User checkAuth(String jwt) {
        try {
            Claims claims = jwtService.getAllClaims(jwt);
            Optional<User> optionalUser = userRepo.findByUsername(claims.getSubject());
            User user = optionalUser.orElseThrow(() -> new RuntimeException(""));
            if(jwtService.validateToken(jwt, new PrincipalUserDetails(user))) return user;
            return null;
        } catch (Exception e) {
            // All exceptions thrown are caught here because the most important thing is to determine if the user is authenticated or not
            return null;
        }
    }
}
