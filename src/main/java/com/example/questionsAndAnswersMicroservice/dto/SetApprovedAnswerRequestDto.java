package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SetApprovedAnswerRequestDto
{
    private String answerId;
    private String questionId;
}
