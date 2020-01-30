package com.example.questionsAndAnswersMicroservice.controller;


import com.example.questionsAndAnswersMicroservice.dto.CategoryDto;
import com.example.questionsAndAnswersMicroservice.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryServices categoryServices;

    @PostMapping("/addQuestionToACategory/")
    public ResponseEntity<String> addQuestionToACategory(CategoryDto categoryDto) {
        return categoryServices.addQuestionToACategory(categoryDto);
    }
}
