package com.example.questionsAndAnswersMicroservice.dto;

import lombok.Data;

@Data
public class SearchDto {

        private String profileValue="";
        private String profileId="";
        private String profileType="";

        private String searchId="";

        private String valueType="" ;

        private String questionValue="";
        private String questionId="";
        private Boolean questionStatus=false;

        private String askerProfileId="";
        private String askerProfileName="";

        private int numberOfLikes=0;

        private int numberOfDislikes=0;

        private String approvedAnswerId="";
        private String approvedAnswer="";

        private String approvedAnswererId="";
        private String approvedAnswererProfile="";

        private String categoryId="";
        private String categoryName="";

}
