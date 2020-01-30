package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmojisDto
{
    private String emojisValue;
    private String profileId;
    private String questionOrAnswerId;
}
