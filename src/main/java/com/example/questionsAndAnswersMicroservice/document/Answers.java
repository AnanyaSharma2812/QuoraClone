package com.example.questionsAndAnswersMicroservice.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(value="Answers")
@Getter
@Setter
@ToString
public class Answers
{
    @Id
    private String answerId;
    private String questionId;
    private String answerValue;
    private Boolean answerStatus;

    private String profileIdOfAnswerer;
    private String profileNameOfAnswerer;

    private int numberOfLikes;
    private List<String> likeProfileIdList=new ArrayList<>();

    private int numberOfDislikes;
    private List<String> dislikeProfileIdList=new ArrayList<>();

    private List<String> emojiValue=new ArrayList<>();
    private List<String> emojiProfileIdList=new ArrayList<>();
}
