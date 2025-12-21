package com.testmaker.api.service.user;

import com.testmaker.api.dto.auth.SignupRequest;
import com.testmaker.api.entity.User;
import com.testmaker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface{
    private final UserRepository userRepo;

    @Override
    public User createUser(SignupRequest requestDto) {
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setUsername(requestDto.getUsername());
        user.setPassword(requestDto.getPassword());
        return userRepo.save(user);
    }
}
