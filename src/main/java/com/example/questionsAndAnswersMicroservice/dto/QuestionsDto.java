package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class QuestionsDto
{
    private String questionId="";
    private String questionValue="";
    private Boolean questionStatus=false;

    private String categoryId="";
    private String categoryName="";

    private String askerProfileId="";
    private String askerProfileName="";

    private String profileWhereAskedId="";
    private String profileWhereAskedName="";
    private String profileWhereAskedType="";

    private int numberOfLikes=0;
    private List<String> likeProfileIdList=new ArrayList<>();

    private int numberOfDislikes=0;
    private List<String> dislikeProfileIdList=new ArrayList<>();

    private List<String> emojisValue=new ArrayList<>();
    private List<String> emojisProfileIdList=new ArrayList<>();

    private Boolean isThreadOpen=true;

    private List<String> answerIdList=new ArrayList<>();

    private String approvedAnswerId="";
    private String approvedAnswer="";
    private String approvedAnswererId="";
    private String approvedAnswererProfile="";
}
