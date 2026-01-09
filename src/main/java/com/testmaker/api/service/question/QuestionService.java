package com.testmaker.api.service.question;

import com.testmaker.api.entity.Question;
import com.testmaker.api.entity.User;
import com.testmaker.api.exception.ResourceNotFoundException;
import com.testmaker.api.repository.QuestionRepository;
import com.testmaker.api.service.user.PrincipalUserDetailsService;
import com.testmaker.api.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionServiceInterface {
    private final QuestionRepository questionRepo;
    private final UserServiceInterface userService;
    private final PrincipalUserDetailsService principalUserService;

    @Override
    public void deleteQuestion(Long id) {
        Optional<Question> optionalQuestion = questionRepo.findById(id);
        Question question = optionalQuestion.orElseThrow(() -> new ResourceNotFoundException("Question not found! Please check your id and try again"));
        User user = userService.getByUsername(principalUserService.getPrincipal().getUsername());
        if(!question.getTest().getUser().getUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("You do not have necessary permissions to perform this action");
        }
        questionRepo.deleteById(question.getId());
    }
}
