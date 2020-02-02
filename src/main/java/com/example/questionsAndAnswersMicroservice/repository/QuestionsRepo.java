package com.example.questionsAndAnswersMicroservice.repository;

import com.example.questionsAndAnswersMicroservice.document.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsRepo extends MongoRepository<Questions,String> {

    Page<Questions> findByQuestionIdIn(List<String> questionIdList, Pageable paging);
    Page<Questions> findAllByAskerProfileId(String askerProfileId,Pageable paging);
}
