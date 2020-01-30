package com.example.questionsAndAnswersMicroservice.services;

import com.example.questionsAndAnswersMicroservice.dto.DislikesDto;
import com.example.questionsAndAnswersMicroservice.dto.EmojisDto;
import com.example.questionsAndAnswersMicroservice.dto.LikesDto;
import com.example.questionsAndAnswersMicroservice.dto.SetApprovedAnswerRequestDto;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import org.springframework.http.ResponseEntity;

public interface QuestionsServices
{
    ResponseEntity<String> addQuestion(Questions questions);

    ResponseEntity<String> addLikes(LikesDto likesDto);

    ResponseEntity<String> addDislikes(DislikesDto dislikesDto);

    ResponseEntity<String> addEmojis(EmojisDto emojisDto);

    ResponseEntity<String> setApprovedAnswer(SetApprovedAnswerRequestDto setApprovedAnswerRequestDto);

    ResponseEntity<Boolean> isThreadOpenCheck(String questionId);

    ResponseEntity<String> setQuestionStatus(String questionId,Boolean status);

    ResponseEntity<String> getProfileIdByQuestionId(String questionId);

    ResponseEntity<Boolean> getQuestionStatus(String questionId);
}
