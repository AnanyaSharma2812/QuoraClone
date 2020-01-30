package com.example.questionsAndAnswersMicroservice.services.servicesImpl;

import com.example.questionsAndAnswersMicroservice.dto.DislikesDto;
import com.example.questionsAndAnswersMicroservice.dto.EmojisDto;
import com.example.questionsAndAnswersMicroservice.dto.LikesDto;
import com.example.questionsAndAnswersMicroservice.dto.SetApprovedAnswerRequestDto;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.example.questionsAndAnswersMicroservice.repository.QuestionsRepo;
import com.example.questionsAndAnswersMicroservice.services.QuestionsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionsServicesImpl implements QuestionsServices {

    @Autowired
    QuestionsRepo questionsRepo;

    @Override
    public ResponseEntity<Boolean> getQuestionStatus(String questionId) {
        Optional<Questions> questions=questionsRepo.findById(questionId);
        if(questions.isPresent()) {
            return new ResponseEntity<Boolean>(questions.get().getQuestionStatus(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<String> addQuestion(Questions questions) {

        questions.setIsThreadOpen(true);
        questions.setApprovedAnswerId("");
        // set questions of public profiles to true status by default
        String profileType=questions.getProfileWhereAskedType();
        if(profileType.equals("public")){
            questions.setQuestionStatus(true);
        }

        // set questions of private or business profiles to false status by default
        if(profileType.equals("business") || profileType.equals("private") || profileType.equals("organization")) {
            questions.setQuestionStatus(false);
        }


        return new ResponseEntity<String>(questionsRepo.save(questions).getQuestionId(),HttpStatus.OK);
    }

    @Override
    //for moderators
    public ResponseEntity<String> setQuestionStatus(String questionId, Boolean status) {

        Optional<Questions> questions=questionsRepo.findById(questionId);
        if(questions.isPresent()) {

            if(questions.get().getProfileWhereAskedType().equals("business") ||
                    questions.get().getProfileWhereAskedType().equals("private") ||
                    questions.get().getProfileWhereAskedType().equals("organization")) {

                questions.get().setQuestionStatus(status);
            }
            return new ResponseEntity<String>(HttpStatus.OK);

        }

        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> addDislikes(DislikesDto dislikesDto) {

        Optional<Questions> questions=questionsRepo.findById(dislikesDto.getQuestionOrAnswerId());
        if(questions.isPresent()) {

        List<String> list=questions.get().getDislikeProfileIdList();
        list.add(dislikesDto.getProfileId());
        questions.get().setDislikeProfileIdList(list);
        questions.get().setNumberOfDislikes(questions.get().getNumberOfDislikes()+1);

        questionsRepo.save(questions.get());

        return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> addEmojis(EmojisDto emojisDto) {

        Optional<Questions> questions=questionsRepo.findById(emojisDto.getQuestionOrAnswerId());
        if(questions.isPresent()) {

            List<String> listOfId=questions.get().getEmojisProfileIdList();
            List<String> listOfEmoji=questions.get().getEmojisValue();

            listOfId.add(emojisDto.getProfileId());
            questions.get().setDislikeProfileIdList(listOfId);

            listOfEmoji.add(emojisDto.getEmojisValue());
            questions.get().setEmojisValue(listOfEmoji);

            questionsRepo.save(questions.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> getProfileIdByQuestionId(String questionId) {
        Optional<Questions> questions = questionsRepo.findById(questionId);
        if (questions.isPresent()) {
            return new ResponseEntity<String>(questions.get().getAskerProfileId(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public ResponseEntity<String> addLikes(LikesDto likesDto)
    {
        Optional<Questions> questions=questionsRepo.findById(likesDto.getQuestionOrAnswerId());
        if(questions.isPresent()) {

            List<String> list=questions.get().getLikeProfileIdList();
            list.add(likesDto.getProfileId());
            questions.get().setLikeProfileIdList(list);
            questions.get().setNumberOfLikes(questions.get().getNumberOfLikes()+1);

            questionsRepo.save(questions.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> setApprovedAnswer(SetApprovedAnswerRequestDto setApprovedAnswerRequestDto) {
        Optional<Questions> questions=questionsRepo.findById(setApprovedAnswerRequestDto.getQuestionId());
        if(questions.isPresent()) {

            questions.get().setApprovedAnswerId(setApprovedAnswerRequestDto.getAnswerId());
            questions.get().setIsThreadOpen(false);

            questionsRepo.save(questions.get());


            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<Boolean> isThreadOpenCheck(String questionId) {
        Optional<Questions> questions=questionsRepo.findById(questionId);
        if(questions.isPresent())
        {
            if(!questions.get().getApprovedAnswerId().equals(""))
            {
                questions.get().setIsThreadOpen(false);
            }
            return new ResponseEntity<Boolean>(questions.get().getIsThreadOpen(),HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);

    }
}
