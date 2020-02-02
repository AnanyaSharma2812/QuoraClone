package com.example.questionsAndAnswersMicroservice.repository;

import com.example.questionsAndAnswersMicroservice.document.Answers;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswersRepo extends MongoRepository<Answers,String> {

    Page<Answers> findAllByQuestionId(String questionId, Pageable paging);
    Page<Answers> findAllByProfileIdOfAnswerer(String userId,Pageable paging);
}
