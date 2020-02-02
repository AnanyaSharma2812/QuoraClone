package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SetApprovedAnswerRequestDto
{
    private String answerId="";
    private String answer="";
    private String profileId="";
    private String profileName="";
    private String questionId="";
}
