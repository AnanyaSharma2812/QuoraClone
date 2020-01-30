package com.example.questionsAndAnswersMicroservice.repository;

import com.example.questionsAndAnswersMicroservice.document.Questions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsRepo extends MongoRepository<Questions,String>
{
    List<Questions> getAll(PageRequest pageRequest);
}
