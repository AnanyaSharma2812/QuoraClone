package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Data;

@Data
public class AnswersNotifResponseDto
{

        String questionAskerId="";
        String questionAskerName="";
        String taggedProfileId="";
        String taggedProfileType="";
        String taggedProfileName="";
        String category="";
        String answerUserId="";
        String answerUserName="";
        Boolean isApproved=false;

}
