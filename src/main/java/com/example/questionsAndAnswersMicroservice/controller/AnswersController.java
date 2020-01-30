package com.example.questionsAndAnswersMicroservice.controller;

import com.example.questionsAndAnswersMicroservice.dto.AnswersDto;
import com.example.questionsAndAnswersMicroservice.dto.DislikesDto;
import com.example.questionsAndAnswersMicroservice.dto.EmojisDto;
import com.example.questionsAndAnswersMicroservice.dto.LikesDto;
import com.example.questionsAndAnswersMicroservice.document.Answers;
import com.example.questionsAndAnswersMicroservice.services.AnswersServices;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
public class AnswersController
{

    @Autowired
    AnswersServices answersServices;

    //to add answers for a particular question
    @PostMapping("/addAnswer")
    public ResponseEntity<String> addAnswer(@RequestBody AnswersDto answersDto){
        Answers answers = new Answers();
        BeanUtils.copyProperties(answersDto, answers);
        return answersServices.addAnswer(answers);
    }

    //to add likes to a particular question or answer
    @PutMapping("/addLikes")
    public ResponseEntity<String> addLikes(@RequestBody LikesDto likesDto){
        return answersServices.addLikes(likesDto);
    }

    //to add likes to a particular question or answer
    @PutMapping("/addDislikes")
    public ResponseEntity<String> addDislikes(@RequestBody DislikesDto dislikesDto){
        return answersServices.addDislikes(dislikesDto);
    }


    //to add emojis to a particular question or answer
    @PutMapping("/addEmojis")
    public ResponseEntity<String> addEmojis(@RequestBody EmojisDto emojisDto){
        return answersServices.addEmojis(emojisDto);
    }


    //to get profileId by answerId
    @GetMapping("/getProfileIdByAnswerId/{answerId}")
    public ResponseEntity<String> getProfileIdByAnswerId(@PathVariable("answerId")String answerId) {
        return answersServices.getProfileIdByAnswerId(answerId);
    }

    //to set status of an answer by moderator
    @PutMapping("/setAnswerStatus/{answerId}/{status}")
    public ResponseEntity<String> setAnswerStatus(@PathVariable("answerId") String answerId,@PathVariable("status")Boolean status){
        return answersServices.setAnswerStatus(answerId,status);
    }

    //to get status of an answer
    @GetMapping("/getAnswerStatus/{answerId}")
    public ResponseEntity<Boolean> getAnswerStatus(@PathVariable("answerId") String answerId){
        return answersServices.getAnswerStatus(answerId);
    }


}
