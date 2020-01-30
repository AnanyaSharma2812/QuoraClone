package com.example.questionsAndAnswersMicroservice.services.servicesImpl;

import com.example.questionsAndAnswersMicroservice.dto.CategoryDto;
import com.example.questionsAndAnswersMicroservice.document.Category;
import com.example.questionsAndAnswersMicroservice.repository.CategoryRepo;
import com.example.questionsAndAnswersMicroservice.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServicesImpl implements CategoryServices {

    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public ResponseEntity<String> addQuestionToACategory(CategoryDto categoryDto) {

        Optional<Category> category=categoryRepo.findById(categoryDto.getCategoryId());
        if(category.isPresent()){
        List<String> list=category.get().getQuestionIdList();
        list.add(categoryDto.getQuestionId());
        categoryRepo.save(category.get());
        return new ResponseEntity<String>(HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }
}
