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
import com.testmaker.api.exception.ResourceNotFoundException;
import com.testmaker.api.repository.TestRepository;
import com.testmaker.api.service.user.PrincipalUserDetailsService;
import com.testmaker.api.service.user.UserServiceInterface;
import com.testmaker.api.utils.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

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
                if(questionRequest.getQuestion() != null) {
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
        }

        test.setQuestions(questions);
        return testRepo.save(test);
    }

    @Override
    public Test saveExistingTestProgress(SaveTestRequest requestDto, Long id) {
        Optional<Test> optionalTest = testRepo.findById(id);
        Test test = optionalTest.orElseThrow(() -> new ResourceNotFoundException("Test not found! Please check your id and try again"));

        // Confirm that the user attempting to update the test is the creator of the test
        User user = userService.getByUsername(principalUserService.getPrincipal().getUsername());
        if(!test.getUser().getUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("You do not have necessary permissions to perform this action");
        }

        // When updating test data, a user can provide null values, but they will be ignored

        if(requestDto.getTitle() != null && !requestDto.getTitle().equals(test.getTitle())) {
            test.setTitle(requestDto.getTitle());
        }

        if(requestDto.getDuration() != null && !Objects.equals(requestDto.getDuration(), test.getDuration())) {
            test.setDuration(requestDto.getDuration());
        }

        if(requestDto.getQuestions() != null) {
            // Add all the test question texts in a set so that we can detect if a question exists or not
            // Users should be able to submit data that already exists and/or new test data
            Set<String> testQuestions = new HashSet<>(); // Stores the question property of the Question entity object
            if(test.getQuestions() != null) {
                for(Question question : test.getQuestions()) testQuestions.add(question.getQuestion());
            }

            for(SaveQuestionRequest questionRequest : requestDto.getQuestions()) {
                // To avoid saving existing data as new fields, check that the question text does not exist in the test questions
                if(questionRequest.getQuestion() != null) {
                    if(!testQuestions.contains(questionRequest.getQuestion())) {
                        Question question = new Question();
                        question.setQuestion(questionRequest.getQuestion());

                        Set<Answer> answers = new HashSet<>();
                        if(questionRequest.getAnswers() != null) {
                            // If the question does not exist in a list of questions then it is a new question so are the answers
                            for(SaveAnswerRequest answerRequest : questionRequest.getAnswers()) {
                                if(answerRequest.getAnswer() != null) {
                                    Answer answer = new Answer();
                                    answer.setQuestion(question);
                                    answer.setAnswer(answerRequest.getAnswer());
                                    answer.setIsCorrect(answerRequest.getIsCorrect());
                                    answers.add(answer);
                                }
                            }
                        }

                        // Since we are creating a new question, a type is required
                        // Throw an exception if the type is not provided or it is invalid
                        switch (questionRequest.getType()) {
                            case "true-or-false" -> question.setType(QuestionType.TRUE_OR_FALSE);
                            case "multiple-choice" -> question.setType(QuestionType.MULTIPLE_CHOICE);
                            case "fill-the-sentence" -> question.setType(QuestionType.FILL_THE_SENTENCE);
                        }

                        question.setAnswers(answers);
                        question.setTest(test);

                        test.getQuestions().add(question);
                    } else {
                        // The user has potentially updated question details
                        // There is no need to check if test questions are null because they cannot be null if they contain the request question text
                        Question question = null;
                        for(Question q : test.getQuestions()) {
                            if(q.getQuestion().equals(questionRequest.getQuestion())) {
                                question = q;
                                break;
                            }
                        }

                        // At this point the question set above will never be null because if it is there on the testQuestions set then it is there on the test
                        if(question.getAnswers() != null) {
                            Set<String> questionAnswers = new HashSet<>();
                            for(Answer answer : question.getAnswers()) questionAnswers.add(answer.getAnswer());

                            if(questionRequest.getAnswers() != null){
                                for(SaveAnswerRequest answerRequest : questionRequest.getAnswers()) {
                                    if(answerRequest.getAnswer() != null) {
                                        if(!questionAnswers.contains(answerRequest.getAnswer())) {
                                            // A new answer is being created
                                            Answer answer = new Answer();
                                            answer.setQuestion(question);
                                            answer.setAnswer(answerRequest.getAnswer());
                                            answer.setIsCorrect(answerRequest.getIsCorrect());
                                            question.getAnswers().add(answer);

                                        } else {
                                            // An existing answer is being updated
                                            Answer answer = null;
                                            for(Answer a : question.getAnswers()) {
                                                if(a.getAnswer().equals(answerRequest.getAnswer())){
                                                    answer = a;
                                                    break;
                                                }
                                            }

                                            answer.setAnswer(answerRequest.getAnswer());
                                            answer.setIsCorrect(answerRequest.getIsCorrect());
                                        }
                                    }
                                }
                            }
                        }

                        if(questionRequest.getType() != null) {
                            switch (questionRequest.getType()) {
                                case "true-or-false" -> question.setType(QuestionType.TRUE_OR_FALSE);
                                case "multiple-choice" -> question.setType(QuestionType.MULTIPLE_CHOICE);
                                case "fill-the-sentence" -> question.setType(QuestionType.FILL_THE_SENTENCE);
                            }
                        }
                    }
                }
            }
        }

        return testRepo.save(test);
    }

    @Override
    public Collection<Test> getAllMyTests() {
        User user = userService.getByUsername(principalUserService.getPrincipal().getUsername());
        return testRepo.getAllByUser(user);
    }

    @Override
    public Test getTestById(Long id) {
        Optional<Test> optionalTest = testRepo.findById(id);
        return optionalTest.orElseThrow(() -> new ResourceNotFoundException("Test not found! Please check your test id and try again"));
    }

    @Override
    public void deleteById(Long id) {
        Test test = this.getTestById(id);
        User user = userService.getByUsername(principalUserService.getPrincipal().getUsername());
        if(!test.getUser().getUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("You do not have necessary permissions to perform this action");
        }
        testRepo.deleteById(id);
    }
}
