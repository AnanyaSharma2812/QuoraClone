package com.example.questionsAndAnswersMicroservice.controller;

import com.example.questionsAndAnswersMicroservice.dto.*;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.example.questionsAndAnswersMicroservice.repository.QuestionsRepo;
import com.example.questionsAndAnswersMicroservice.services.QuestionsServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuestionsController {

    @Autowired
    QuestionsServices questionsServices;

    @Autowired
    private QuestionsRepo questionsRepo;

    //to add new questions in the database
    @PostMapping("/addQuestion")
    public ResponseEntity<String> addQuestion(@RequestHeader("userId")String userId,@RequestHeader("name") String name,@RequestBody QuestionsDto questionsDto) throws JsonProcessingException {
        Questions questions = new Questions();
        questionsDto.setAskerProfileId(userId);
        questionsDto.setAskerProfileName(name);
        BeanUtils.copyProperties(questionsDto, questions);
        return questionsServices.addQuestion(questions);
    }

    //to add likes to a particular question or answer
    @PutMapping("/addLikes")
    public ResponseEntity<String> addLikes(@RequestHeader("userId")String userId,@RequestHeader("name") String name,@RequestBody LikesDto likesDto) throws JsonProcessingException {
        likesDto.setProfileId(userId);
        likesDto.setProfileName(name);
        return questionsServices.addLikes(likesDto);
    }

    //to add likes to a particular question or answer
    @PutMapping("/addDislikes")
    public ResponseEntity<String> addDislikes(@RequestHeader("userId")String userId,@RequestHeader("name") String name,@RequestBody DislikesDto dislikesDto) throws JsonProcessingException {
        dislikesDto.setProfileId(userId);
        dislikesDto.setProfileName(name);
        return questionsServices.addDislikes(dislikesDto);
    }


    //to add emojis to a particular question or answer
    @PutMapping("/addEmojis")
    public ResponseEntity<String> addEmojis(@RequestHeader("userId")String userId,@RequestHeader("name") String name,@RequestBody EmojisDto emojisDto) throws JsonProcessingException {
        emojisDto.setProfileId(userId);
        emojisDto.setProfileName(name);
        return questionsServices.addEmojis(emojisDto);
    }


    //to set one answer as approved and close thread
    @PostMapping("/setApprovedAnswer")
    public ResponseEntity<String> setApprovedAnswer(@RequestHeader("userId")String userId,@RequestHeader("name") String name,@RequestBody SetApprovedAnswerRequestDto setApprovedAnswerRequestDto) throws JsonProcessingException {
        setApprovedAnswerRequestDto.setProfileId(userId);
        setApprovedAnswerRequestDto.setProfileName(name);
        return questionsServices.setApprovedAnswer(setApprovedAnswerRequestDto);
    }

    //to check if the thread is open
    @GetMapping("/isThreadOpenCheck/{questionId}")
    public ResponseEntity<Boolean> isThreadOpenCheck(@PathVariable String questionId) {
        return questionsServices.isThreadOpenCheck(questionId);
    }


    //to set status of a question by moderator
    @PutMapping("/setQuestionStatus/{questionId}/{status}")
    public ResponseEntity<String> setQuestionStatus(@PathVariable("questionId") String questionId,@PathVariable("status")Boolean status) throws JsonProcessingException {
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
    public ResponseEntity<Page<Questions>> getAllQuestions(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                           @RequestParam(defaultValue = "5") Integer pageSize,
                                                           @RequestParam(defaultValue = "date") String sortBy) {
        return new ResponseEntity<>(questionsServices.getAllQuestions(pageNumber, pageSize,sortBy), HttpStatus.OK);
    }

    @PostMapping("/getQuestionsOfSelectedCategories")
    public ResponseEntity<Page<Questions>> getQuestionsOfSelectedCategories(@RequestBody GetQuestionsOfSelectedCategoriesRequestDto getQuestionsRequestDto) {
        return new ResponseEntity<>(questionsServices.getQuestionsOfSelectedCategories(getQuestionsRequestDto), HttpStatus.OK);
    }


    @GetMapping("/getQuestionsByUserId")
    public ResponseEntity<Page<Questions>> getQuestionsByUserId(@RequestHeader("userId")String userId,
                                                                @RequestParam(defaultValue = "0") Integer pageNumber,
                                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                                @RequestParam(defaultValue = "date") String sortBy)
    {
        return new ResponseEntity<>(questionsServices.getQuestionsByUserId(userId,pageNumber,pageSize,sortBy),HttpStatus.OK);

    }



}