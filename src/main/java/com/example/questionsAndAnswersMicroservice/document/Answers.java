package com.example.questionsAndAnswersMicroservice.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(value="Answers")
@Getter
@Setter
@ToString
public class Answers
{
    @Id
    private String answerId="";
    private String questionId="";
    private String question="";
    private String questionAskerId="";
    private String questionAskerName="";

    private String profileWhereAskedId="";
    private String profileWhereAskedName="";
    private String profileWhereAskedType="";

    private String answerValue="";
    private Boolean answerStatus=false;

    private Date date=new Date();

    private String profileIdOfAnswerer="";
    private String profileNameOfAnswerer="";


    private int numberOfLikes=0 ;
    private List<String> likeProfileIdList=new ArrayList<>();

    private int numberOfDislikes=0;
    private List<String> dislikeProfileIdList=new ArrayList<>();

    private List<String> emojiValue=new ArrayList<>();
    private List<String> emojiProfileIdList=new ArrayList<>();
}
