package com.example.questionsAndAnswersMicroservice.services;

import com.example.questionsAndAnswersMicroservice.dto.DislikesDto;
import com.example.questionsAndAnswersMicroservice.dto.EmojisDto;
import com.example.questionsAndAnswersMicroservice.dto.LikesDto;
import com.example.questionsAndAnswersMicroservice.document.Answers;
import org.springframework.http.ResponseEntity;

public interface AnswersServices {
    ResponseEntity<String> addAnswer(Answers answers);

    ResponseEntity<String> addDislikes(DislikesDto dislikesDto);

    ResponseEntity<String> addEmojis(EmojisDto emojisDto);

    ResponseEntity<String> addLikes(LikesDto likesDto);

    ResponseEntity<String> getProfileIdByAnswerId(String answerId);

    ResponseEntity<String> setAnswerStatus(String answerId, Boolean status);

    ResponseEntity<Boolean> getAnswerStatus(String answerId);
}
