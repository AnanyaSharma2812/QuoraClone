package com.example.questionsAndAnswersMicroservice.controller;


import com.example.questionsAndAnswersMicroservice.dto.CategoryDto;
import com.example.questionsAndAnswersMicroservice.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

    @Autowired
    CategoryServices categoryServices;

    @PostMapping("/addCategories")
    public ResponseEntity<String> addCategories(@RequestBody CategoryDto categoryDto) {
        return categoryServices.addCategories(categoryDto);
    }

}
