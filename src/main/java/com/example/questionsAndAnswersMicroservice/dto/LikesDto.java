package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LikesDto
{
    private String profileId="";
    private String questionOrAnswerId="";
    private String profileName="";
    private String askerOrAnswererId="";

}
