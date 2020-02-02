package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Data;

@Data
public class ReactionDto
{
    String reactedUserName="";
    String reactedUserId="";
    String reactionType="";
    String category="";
    String onWhomReactedId="";
    String postType="";
}
