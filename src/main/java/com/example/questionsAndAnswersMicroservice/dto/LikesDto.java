package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LikesDto
{
    String profileId;
    String questionOrAnswerId;

}
