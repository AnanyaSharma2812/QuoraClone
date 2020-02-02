package com.example.questionsAndAnswersMicroservice.services;

import com.example.questionsAndAnswersMicroservice.dto.*;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface QuestionsServices
{
    ResponseEntity<String> addQuestion(Questions questions) throws JsonProcessingException;

    ResponseEntity<String> addLikes(LikesDto likesDto) throws JsonProcessingException;

    ResponseEntity<String> addDislikes(DislikesDto dislikesDto) throws JsonProcessingException;

    ResponseEntity<String> addEmojis(EmojisDto emojisDto) throws JsonProcessingException;

    ResponseEntity<String> setApprovedAnswer(SetApprovedAnswerRequestDto setApprovedAnswerRequestDto) throws JsonProcessingException;

    ResponseEntity<Boolean> isThreadOpenCheck(String questionId);

    ResponseEntity<String> setQuestionStatus(String questionId,Boolean status) throws JsonProcessingException;

    ResponseEntity<String> getProfileIdByQuestionId(String questionId);

    ResponseEntity<Boolean> getQuestionStatus(String questionId);

    Page<Questions> getAllQuestions(Integer pageNumber, Integer pageSize,String sortBy);

    Page<Questions> getQuestionsOfSelectedCategories(GetQuestionsOfSelectedCategoriesRequestDto getQuestionsRequestDto);

    Page<Questions> getQuestionsByUserId(String userId,Integer pageNumber,Integer pageSize,String sortBy);


}
