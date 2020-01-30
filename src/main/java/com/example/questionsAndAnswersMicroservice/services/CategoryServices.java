package com.example.questionsAndAnswersMicroservice.services;

import com.example.questionsAndAnswersMicroservice.dto.CategoryDto;
import org.springframework.http.ResponseEntity;

public interface CategoryServices {
    ResponseEntity<String> addQuestionToACategory(CategoryDto categoryDto);
}
