package com.example.questionsAndAnswersMicroservice.controller;

import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.example.questionsAndAnswersMicroservice.dto.AnswersDto;
import com.example.questionsAndAnswersMicroservice.dto.DislikesDto;
import com.example.questionsAndAnswersMicroservice.dto.EmojisDto;
import com.example.questionsAndAnswersMicroservice.dto.LikesDto;
import com.example.questionsAndAnswersMicroservice.document.Answers;
import com.example.questionsAndAnswersMicroservice.services.AnswersServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AnswersController
{

    @Autowired
    AnswersServices answersServices;

    //to add answers for a particular question
    @PostMapping("/addAnswer")
    public ResponseEntity<String> addAnswer(@RequestHeader("userId")String userId,@RequestHeader("name") String name,@RequestBody AnswersDto answersDto) throws JsonProcessingException {
        Answers answers = new Answers();
        answersDto.setProfileIdOfAnswerer(userId);
        answersDto.setProfileNameOfAnswerer(name);
        BeanUtils.copyProperties(answersDto, answers);
        return answersServices.addAnswer(answers);
    }

    //to add likes to a particular question or answer
    @PutMapping("/addLikes")
    public ResponseEntity<String> addLikes(@RequestHeader("userId")String userId,@RequestHeader("name") String name,@RequestBody LikesDto likesDto) throws JsonProcessingException {
        likesDto.setProfileId(userId);
        likesDto.setProfileName(name);
        return answersServices.addLikes(likesDto);
    }

    //to add likes to a particular question or answer
    @PutMapping("/addDislikes")
    public ResponseEntity<String> addDislikes(@RequestHeader("userId")String userId,@RequestHeader("name") String name,@RequestBody DislikesDto dislikesDto) throws JsonProcessingException {
        dislikesDto.setProfileName(name);
        dislikesDto.setProfileId(userId);
        return answersServices.addDislikes(dislikesDto);
    }


    //to add emojis to a particular question or answer
    @PutMapping("/addEmojis")
    public ResponseEntity<String> addEmojis(@RequestBody EmojisDto emojisDto) throws JsonProcessingException {
        return answersServices.addEmojis(emojisDto);
    }


    //to get profileId by answerId
    @GetMapping("/getProfileIdByAnswerId/{answerId}")
    public ResponseEntity<String> getProfileIdByAnswerId(@PathVariable("answerId")String answerId) {
        return answersServices.getProfileIdByAnswerId(answerId);
    }

    //to set status of an answer by moderator
    @PutMapping("/setAnswerStatus/{answerId}/{status}")
    public ResponseEntity<String> setAnswerStatus(@PathVariable("answerId") String answerId,@PathVariable("status")Boolean status) throws JsonProcessingException {
        return answersServices.setAnswerStatus(answerId,status);
    }

    //to get status of an answer
    @GetMapping("/getAnswerStatus/{answerId}")
    public ResponseEntity<Boolean> getAnswerStatus(@PathVariable("answerId") String answerId){
        return answersServices.getAnswerStatus(answerId);
    }

    //get all answers of a question
    @GetMapping("/getAllAnswersOfAQuestion")
    public ResponseEntity<Page<Answers>> getAllAnswersOfAQuestion(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                           @RequestParam(defaultValue = "5") Integer pageSize,
                                                           @RequestParam(defaultValue = "date") String sortBy,@RequestParam("questionId")String questionId)
    {
        return new ResponseEntity<>(answersServices.getAllAnswersOfAQuestion(pageNumber, pageSize,sortBy,questionId), HttpStatus.OK);
    }

    @GetMapping("/getAnswersByUserId")
    public ResponseEntity<Page<Answers>> getAnswersByUserId(@RequestHeader("userId")String userId,
                                                              @RequestParam(defaultValue = "0") Integer pageNumber,
                                                              @RequestParam(defaultValue = "5") Integer pageSize,
                                                              @RequestParam(defaultValue = "date") String sortBy)
    {
        return new ResponseEntity<>(answersServices.getAnswersByUserId(userId,pageNumber,pageSize,sortBy),HttpStatus.OK);
    }

}
