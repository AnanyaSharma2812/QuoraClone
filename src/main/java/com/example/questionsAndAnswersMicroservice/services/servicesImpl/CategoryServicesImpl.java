package com.example.questionsAndAnswersMicroservice.services.servicesImpl;

import com.example.questionsAndAnswersMicroservice.document.Category;
import com.example.questionsAndAnswersMicroservice.dto.CategoryDto;
import com.example.questionsAndAnswersMicroservice.repository.CategoryRepo;
import com.example.questionsAndAnswersMicroservice.services.CategoryServices;
import org.springframework.beans.BeanUtils;
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
    public String addQuestionToACategory(String categoryId,String questionId) {

        Optional<Category> category=categoryRepo.findById(categoryId);
        if(category.isPresent()){
            List<String> list=category.get().getQuestionIdList();
            list.add(questionId);
            categoryRepo.save(category.get());
        return "1";
        }
        return "0";
    }

    @Override
    public ResponseEntity<String> addCategories(CategoryDto categoryDto) {
        Category category=new Category();

        BeanUtils.copyProperties(categoryDto,category);
        categoryRepo.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
