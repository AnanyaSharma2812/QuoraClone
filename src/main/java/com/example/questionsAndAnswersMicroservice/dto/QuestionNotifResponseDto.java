package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Data;

@Data
public class QuestionNotifResponseDto
{

        String askerId="";
        String askerUserName="";
        String taggedProfileId="";
        String taggedProfileType="";
        String taggedProfileName="";
        String category="";
        Boolean isApproved=false;

}
