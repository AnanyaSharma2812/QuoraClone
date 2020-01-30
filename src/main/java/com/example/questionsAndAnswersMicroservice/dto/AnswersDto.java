package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class AnswersDto
{
    private String answerId;
    private String questionId;
    private String answerValue;

    private String profileIdOfAnswerer;
    private String profileNameOfAnswerer;

    private int numberOfLikes;
    private List<String> likeProfileIdList=new ArrayList<>();

    private int numberOfDislikes;
    private List<String> dislikeProfileIdList=new ArrayList<>();

    private List<String> emojiValue=new ArrayList<>();
    private List<String> emojiProfileIdList=new ArrayList<>();

}
