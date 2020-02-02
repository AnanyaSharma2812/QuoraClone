package com.example.questionsAndAnswersMicroservice.repository;

import com.example.questionsAndAnswersMicroservice.document.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends MongoRepository<Category,String> {
    Optional<Category> findByCategoryIdIgnoreCase(String categoryId);
}
