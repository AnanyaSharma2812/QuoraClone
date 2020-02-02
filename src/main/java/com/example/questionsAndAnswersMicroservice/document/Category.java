package com.example.questionsAndAnswersMicroservice.document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(value="Category")
@Getter
@Setter
@ToString
public class Category
{
    @Id
    private String categoryId="";
    private String categoryName="";
    private List<String> questionIdList=new ArrayList<>();

}
