package com.example.questionsAndAnswersMicroservice.services;

import com.example.questionsAndAnswersMicroservice.dto.CategoryDto;
import org.springframework.http.ResponseEntity;

public interface CategoryServices {

    String addQuestionToACategory(String categoryId,String questionId);

    ResponseEntity<String> addCategories(CategoryDto categoryDto);
}
