package com.example.questionsAndAnswersMicroservice.controller;

import com.example.questionsAndAnswersMicroservice.dto.*;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.example.questionsAndAnswersMicroservice.repository.QuestionsRepo;
import com.example.questionsAndAnswersMicroservice.services.QuestionsServices;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
public class QuestionsController {

    @Autowired
    QuestionsServices questionsServices;

    @Autowired
    private QuestionsRepo questionsRepo;

    //to add new questions in the database
    @PostMapping("/addQuestion")
    public ResponseEntity<String> addQuestion(@RequestBody QuestionsDto questionsDto) {
        Questions questions = new Questions();
        BeanUtils.copyProperties(questionsDto, questions);
        return questionsServices.addQuestion(questions);
    }

    //to add likes to a particular question or answer
    @PutMapping("/addLikes")
    public ResponseEntity<String> addLikes(@RequestBody LikesDto likesDto) {
        return questionsServices.addLikes(likesDto);
    }

    //to add likes to a particular question or answer
    @PutMapping("/addDislikes")
    public ResponseEntity<String> addDislikes(@RequestBody DislikesDto dislikesDto) {
        return questionsServices.addDislikes(dislikesDto);
    }


    //to add emojis to a particular question or answer
    @PutMapping("/addEmojis")
    public ResponseEntity<String> addEmojis(@RequestBody EmojisDto emojisDto) {
        return questionsServices.addEmojis(emojisDto);
    }


    //to set one answer as approved and close thread
    @PostMapping("/setApprovedAnswer")
    public ResponseEntity<String> setApprovedAnswer(@RequestBody SetApprovedAnswerRequestDto setApprovedAnswerRequestDto) {
        return questionsServices.setApprovedAnswer(setApprovedAnswerRequestDto);
    }

    //to check if the thread is open
    @GetMapping("/isThreadOpenCheck/{questionId}")
    public ResponseEntity<Boolean> isThreadOpenCheck(@PathVariable String questionId) {
        return questionsServices.isThreadOpenCheck(questionId);
    }


    //to set status of a question by moderator
    @PutMapping("/setQuestionStatus/{questionId}/{status}")
    public ResponseEntity<String> setQuestionStatus(@PathVariable("questionId") String questionId,@PathVariable("status")Boolean status){
        return questionsServices.setQuestionStatus(questionId,status);
    }

    //to get status of a question
    @GetMapping("/getQuestionStatus/{questionId}")
    public ResponseEntity<Boolean> getQuestionStatus(@PathVariable("questionId") String questionId){
        return questionsServices.getQuestionStatus(questionId);
    }

    //to get profileId by questionId
    @GetMapping("/getProfileIdByQuestionId/{questionId}")
    public ResponseEntity<String> getProfileIdByQuestionId(@PathVariable("questionId")String questionId) {
        return questionsServices.getProfileIdByQuestionId(questionId);
    }

    //to get questions for guest homepage
    @GetMapping("/getAllQuestions")
    public void getAll(){
    }



}