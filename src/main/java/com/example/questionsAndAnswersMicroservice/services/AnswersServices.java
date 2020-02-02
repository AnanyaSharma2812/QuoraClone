package com.example.questionsAndAnswersMicroservice.services;

import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.example.questionsAndAnswersMicroservice.dto.DislikesDto;
import com.example.questionsAndAnswersMicroservice.dto.EmojisDto;
import com.example.questionsAndAnswersMicroservice.dto.LikesDto;
import com.example.questionsAndAnswersMicroservice.document.Answers;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface AnswersServices {
    ResponseEntity<String> addAnswer(Answers answers) throws JsonProcessingException;

    ResponseEntity<String> addDislikes(DislikesDto dislikesDto) throws JsonProcessingException;

    ResponseEntity<String> addEmojis(EmojisDto emojisDto) throws JsonProcessingException;

    ResponseEntity<String> addLikes(LikesDto likesDto) throws JsonProcessingException;

    ResponseEntity<String> getProfileIdByAnswerId(String answerId);

    ResponseEntity<String> setAnswerStatus(String answerId, Boolean status) throws JsonProcessingException;

    ResponseEntity<Boolean> getAnswerStatus(String answerId);

    Page<Answers> getAllAnswersOfAQuestion(Integer pageNumber, Integer pageSize, String sortBy, String questionId);

    Page<Answers> getAnswersByUserId(String userId, Integer pageNumber, Integer pageSize, String sortBy);
}
