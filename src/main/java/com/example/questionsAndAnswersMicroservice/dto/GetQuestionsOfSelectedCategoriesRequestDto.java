package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GetQuestionsOfSelectedCategoriesRequestDto {
    List<String> categoryIdList;
    Integer pageNumber = 0;
    Integer pageSize = 10;
    String sortBy = "date";
}
