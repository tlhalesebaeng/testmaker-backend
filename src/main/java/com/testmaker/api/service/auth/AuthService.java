package com.testmaker.api.service.auth;

import com.testmaker.api.dto.auth.*;
import com.testmaker.api.entity.EmailVerificationCode;
import com.testmaker.api.entity.ResetPasswordCode;
import com.testmaker.api.entity.Status;
import com.testmaker.api.entity.User;
import com.testmaker.api.exception.*;
import com.testmaker.api.repository.StatusRepository;
import com.testmaker.api.repository.UserRepository;
import com.testmaker.api.service.cookie.CookieServiceInterface;
import com.testmaker.api.service.jwt.JwtServiceInterface;
import com.testmaker.api.service.user.PrincipalUserDetails;
import com.testmaker.api.utils.AccountStatus;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthServiceInterface {
    private final UserRepository userRepo;
    private final StatusRepository statusRepo;
    private final HttpServletResponse response;
    private final JwtServiceInterface jwtService;
    private final CookieServiceInterface cookieService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${api.email-verification-code.expiration}")
    private Integer emailVerificationCodeExpiration;

    @Value("${api.cookies.auth.expiration}")
    private Long cookieExpiration; // In milliseconds

    @Override
    public User createUser(SignupRequest requestDto) {
        // Validate the uniqueness of the user's email and username
        Optional<User> optionalUser = userRepo.findByUsername(requestDto.getUsername());
        if(optionalUser.isPresent()) {
            if(optionalUser.get().getEmail().equals(requestDto.getEmail())) {
                throw new DuplicateKeyException("A user with this email already exists! Please check your email or use a different one");
            }
            throw new DuplicateKeyException("A user with this username already exists! Please check your username or use a different one");
        }

        Optional<Status> status = statusRepo.findByName(AccountStatus.PENDING_EMAIL_VERIFICATION);
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setStatus(status.orElseThrow(() -> new StatusNotFoundException("Couldn't sign up! Please try again later")));
        // TODO: Send the user an email with the 6 digit verification code
        user.setEmailVerificationCode(new EmailVerificationCode(emailVerificationCodeExpiration));
        return userRepo.save(user);
    }

    @Override
    public User verifyEmailAddress(VerifyCodeRequest requestDto) {
        // Find the user with the provided email verification code
        Optional<User> optionalUser = userRepo.findByEmailVerificationCode(requestDto.getCode());
        User dbUser = optionalUser.orElseThrow(() -> new IncorrectVerificationCodeException("Incorrect code provided! Please check your code and try again"));

        // Confirm that the code has not expired
        EmailVerificationCode userEmailVerificationCode = dbUser.getEmailVerificationCode();
        if(userEmailVerificationCode.getExpiration().isBefore(LocalDateTime.now())) {
            throw new ExpiredCodeException("Email verification code expired! Please request for a new one");
        }

        // Verify the user's status
        if(dbUser.getStatus().getName().equals(AccountStatus.ACTIVE)) {
            throw new IncorrectAccountStatusException("Email already verified! Please continue to login to access your account");
        }

        // Find the active status from the database (Creating a new object will cause consistency issues)
        Optional<Status> optionalActiveStatus = statusRepo.findByName(AccountStatus.ACTIVE);
        Status activeStatus = optionalActiveStatus.orElseThrow(() -> new StatusNotFoundException("Couldn't sign up! Please try again later"));

        // Change the user status to active
        dbUser.setStatus(activeStatus);
        dbUser.setEmailVerificationCode(null);
        User user = userRepo.save(dbUser);

        String token = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_token", token, null));
        return user;
    }

    @Override
    public User forgotPassword(ForgotPasswordRequest requestDto) {
        Optional<User> optionalUser = userRepo.findByUsername(requestDto.getUsername());
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found. Please verify your username and try again"));
        user.setResetPasswordCode(new ResetPasswordCode(emailVerificationCodeExpiration));
        return userRepo.save(user);
    }

    @Override
    public User verifyPasswordResetCode(VerifyCodeRequest requestDto) {
        Optional<User> optionalUser = userRepo.findByPasswordResetCode(requestDto.getCode());
        User user = optionalUser.orElseThrow(() -> new IncorrectVerificationCodeException("Incorrect code provided! Please check your code and try again"));

        // Confirm that the code has not expired
        ResetPasswordCode userResetPasswordCode = user.getResetPasswordCode();
        if(userResetPasswordCode.getExpiration().isBefore(LocalDateTime.now())) {
            throw new ExpiredCodeException("Password reset code expired! Please request for a new one");
        }

        return user;
    }

    @Override
    public User resetPassword(ResetPasswordRequest requestDto, Integer code) {
        // Code cannot be null at this point because the request parameter validators have already validated it
        if(code < 100000 || code > 999999) {
            throw new IncorrectVerificationCodeException("Invalid code provided! Please provide a 6 digit code");
        }

        if(!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match! Please confirm your password");
        }

        Optional<User> optionalUser = userRepo.findByPasswordResetCode(code);
        User dbUser = optionalUser.orElseThrow(() -> new IncorrectVerificationCodeException("Incorrect code provided! Please check your code and try again"));

        // Confirm that the code has not expired
        ResetPasswordCode userResetPasswordCode = dbUser.getResetPasswordCode();
        if(userResetPasswordCode.getExpiration().isBefore(LocalDateTime.now())) {
            throw new ExpiredCodeException("Password reset code expired! Please request for a new one");
        }

        // Verify the user's status
        if(dbUser.getStatus().getName().equals(AccountStatus.PENDING_EMAIL_VERIFICATION)) {
            throw new EmailNotVerifiedException("Email not verified! Please verify your email address");
        }

        dbUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        dbUser.setResetPasswordCode(null);
        User user = userRepo.save(dbUser);

        String token = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_token", token, null));
        return user;
    }

    @Override
    public User login(LoginRequest requestDto) {
        Optional<User> optionalUser = userRepo.findByUsername(requestDto.getUsername());
        User user = optionalUser.orElseThrow(() -> new BadCredentialsException("Incorrect credentials! Please check your credentials and try again"));

        if(user.getStatus().getName().equals(AccountStatus.PENDING_EMAIL_VERIFICATION)) {
            user.setEmailVerificationCode(new EmailVerificationCode(emailVerificationCodeExpiration));
            userRepo.save(user);
            throw new EmailNotVerifiedException("Email not verified! Please verify your email address");
        }

        if(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            String token = jwtService.generateToken(user);
            if(requestDto.getRememberUser() != null && requestDto.getRememberUser()) {
                response.addCookie(cookieService.create("access_token", token, Math.toIntExact(cookieExpiration))); // Max age should be in seconds
            } else {
                response.addCookie(cookieService.create("access_token", token, null)); // Max age should be in seconds
            }
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

    @Override
    public void logout() {
        response.addCookie(cookieService.create("access_token", "", null));
    }
}
