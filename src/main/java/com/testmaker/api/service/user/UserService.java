package com.testmaker.api.service.user;

import com.testmaker.api.entity.User;
import com.testmaker.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface{
    private final UserRepository userRepo;

    @Override
    public User getByUsername(String username) {
        Optional<User> user = userRepo.findByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found. Please verify your username and try again"));
    }
}
