package com.example.questionsAndAnswersMicroservice.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDto
{

    private String categoryName;
    private String categoryId;
    private String questionId;

}
