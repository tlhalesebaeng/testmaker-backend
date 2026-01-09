package com.testmaker.api.service.answer;

import com.testmaker.api.entity.Answer;
import com.testmaker.api.entity.Test;
import com.testmaker.api.entity.User;
import com.testmaker.api.exception.ResourceNotFoundException;
import com.testmaker.api.repository.AnswerRepository;
import com.testmaker.api.service.user.PrincipalUserDetailsService;
import com.testmaker.api.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService implements AnswerServiceInterface{
    private final AnswerRepository answerRepo;
    private final UserServiceInterface userService;
    private final PrincipalUserDetailsService principalUserService;

    @Override
    public void deleteQuestion(Long id) {
        Optional<Answer> optionalAnswer = answerRepo.findById(id);
        Answer answer = optionalAnswer.orElseThrow(() -> new ResourceNotFoundException("Answer not found! Please check your id and try again"));
        User user = userService.getByUsername(principalUserService.getPrincipal().getUsername());
        Test test = answer.getQuestion().getTest();
        if(!test.getUser().getUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("You do not have necessary permissions to perform this action");
        }
        answerRepo.deleteById(answer.getId());
    }
}
