package com.testmaker.api.service.test;

import com.testmaker.api.dto.answer.AnswerRequest;
import com.testmaker.api.dto.answer.SaveAnswerRequest;
import com.testmaker.api.dto.question.QuestionRequest;
import com.testmaker.api.dto.question.SaveQuestionRequest;
import com.testmaker.api.dto.test.CreateTestRequest;
import com.testmaker.api.dto.test.SaveTestRequest;
import com.testmaker.api.entity.Answer;
import com.testmaker.api.entity.Question;
import com.testmaker.api.entity.Test;
import com.testmaker.api.entity.User;
import com.testmaker.api.repository.TestRepository;
import com.testmaker.api.service.user.PrincipalUserDetailsService;
import com.testmaker.api.service.user.UserServiceInterface;
import com.testmaker.api.utils.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestService implements TestServiceInterface {
    private final TestRepository testRepo;
    private final UserServiceInterface userService;
    private final PrincipalUserDetailsService principalUserService;

    @Override
    public Test createTest(CreateTestRequest requestDto) {
        User user = userService.getByUsername(principalUserService.getPrincipal().getUsername());

        Test test = new Test();
        test.setUser(user);
        test.setDuration(requestDto.getDuration());
        test.setTitle(requestDto.getTitle());

        Set<Question> questions = new HashSet<>();
        Set<String> questionTexts = new HashSet<>(); // Used to detect duplicate questions

        for(QuestionRequest questionRequest : requestDto.getQuestions()) {
            if(questionTexts.contains(questionRequest.getQuestion())) {
                throw new DuplicateKeyException("Duplicate question detected! Please make sure that your questions are unique");
            }

            Question question = new Question();
            Set<Answer> answers = new HashSet<>();
            Set<String> answerTexts = new HashSet<>(); // Used to detect duplicate answers

            for(AnswerRequest answerRequest : questionRequest.getAnswers()) {
                if(answerTexts.contains(answerRequest.getAnswer())) {
                    throw new DuplicateKeyException("Duplicate answer detected! Please make sure that your answers are unique");
                }

                Answer answer = new Answer();
                answer.setQuestion(question);
                answer.setAnswer(answerRequest.getAnswer());
                answer.setIsCorrect(answerRequest.getIsCorrect());
                answers.add(answer);

                answerTexts.add(answer.getAnswer());
            }

            // Throw an exception when the user provides a type that is not supported
            switch (questionRequest.getType()) {
                case "true-or-false" -> question.setType(QuestionType.TRUE_OR_FALSE);
                case "multiple-choice" -> question.setType(QuestionType.MULTIPLE_CHOICE);
                case "fill-the-sentence" -> question.setType(QuestionType.FILL_THE_SENTENCE);
            }

            question.setTest(test);
            question.setAnswers(answers);
            question.setQuestion(questionRequest.getQuestion());

            questions.add(question);
            questionTexts.add(question.getQuestion());

        }

        test.setQuestions(questions);

        return testRepo.save(test);
    }

    @Override
    public Test saveNewTestProgress(SaveTestRequest requestDto) {
        User user = userService.getByUsername(principalUserService.getPrincipal().getUsername());

        Test test = new Test();
        test.setUser(user);

        if(requestDto.getTitle() == null) test.setTitle(""); // Test title cannot be null
        else test.setTitle(requestDto.getTitle());

        if(requestDto.getDuration() == null) test.setDuration(0); // Test duration cannot be null
        else test.setDuration(requestDto.getDuration());

        // Duplicates are not checked here since the user's progress should be saved just the way it is
        Set<Question> questions = new HashSet<>();

        if(requestDto.getQuestions() != null) { // Avoids calling List.iterator() method on a null value
            for(SaveQuestionRequest questionRequest : requestDto.getQuestions()) {
                // Don't check if the question text is null because the user might have not decided how to structure the question when saving progress
                Question question = new Question();
                question.setQuestion(questionRequest.getQuestion());

                Set<Answer> answers = new HashSet<>();
                if(questionRequest.getAnswers() != null) { // Avoids calling List.iterator() method on a null value
                    for(SaveAnswerRequest answerRequest : questionRequest.getAnswers()) {
                        if(answerRequest.getAnswer() != null) { // Without the answer text, the answer becomes very useless
                            Answer answer = new Answer();
                            answer.setQuestion(question);
                            answer.setAnswer(answerRequest.getAnswer());
                            answer.setIsCorrect(answerRequest.getIsCorrect());
                            answers.add(answer);
                        }
                    }
                }

                // On the client side, the question type is used to determine which GUI is shown so it cannot be null
                // Throw an exception when the question type is null or invalid
                switch (questionRequest.getType()) {
                    case "true-or-false" -> question.setType(QuestionType.TRUE_OR_FALSE);
                    case "multiple-choice" -> question.setType(QuestionType.MULTIPLE_CHOICE);
                    case "fill-the-sentence" -> question.setType(QuestionType.FILL_THE_SENTENCE);
                }

                question.setTest(test);
                question.setAnswers(answers);

                questions.add(question);
            }
        }

        test.setQuestions(questions);
        return testRepo.save(test);
    }
}
