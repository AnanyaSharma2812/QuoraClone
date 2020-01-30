package com.example.questionsAndAnswersMicroservice.repository;

import com.example.questionsAndAnswersMicroservice.document.Answers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswersRepo extends MongoRepository<Answers,String> {
}
