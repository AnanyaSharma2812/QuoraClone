package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class AnswersDto
{
    private String answerId="";
    private String questionId="";
    private String question="";
    private String questionAskerId="";
    private String questionAskerName="";
    private String answerValue="";

    private String profileWhereAskedId="";
    private String profileWhereAskedName="";
    private String profileWhereAskedType="";

    private String profileIdOfAnswerer="";
    private String profileNameOfAnswerer="";

    private int numberOfLikes=0;
    private List<String> likeProfileIdList=new ArrayList<>();

    private int numberOfDislikes=0;
    private List<String> dislikeProfileIdList=new ArrayList<>();

    private List<String> emojiValue=new ArrayList<>();
    private List<String> emojiProfileIdList=new ArrayList<>();

}
