package com.example.questionsAndAnswersMicroservice.services.servicesImpl;

import com.example.questionsAndAnswersMicroservice.dto.DislikesDto;
import com.example.questionsAndAnswersMicroservice.dto.EmojisDto;
import com.example.questionsAndAnswersMicroservice.dto.LikesDto;
import com.example.questionsAndAnswersMicroservice.document.Answers;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.example.questionsAndAnswersMicroservice.repository.AnswersRepo;
import com.example.questionsAndAnswersMicroservice.repository.QuestionsRepo;
import com.example.questionsAndAnswersMicroservice.services.AnswersServices;
import com.example.questionsAndAnswersMicroservice.services.QuestionsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswersServicesImpl implements AnswersServices {

    @Autowired
    AnswersRepo answersRepo;

    @Autowired
    QuestionsRepo questionsRepo;

    @Autowired
    QuestionsServices questionsServices;


    @Override
    public ResponseEntity<String> addDislikes(DislikesDto dislikesDto) {

        Optional<Answers> answers = answersRepo.findById(dislikesDto.getQuestionOrAnswerId());
        if (answers.isPresent()) {

            List<String> list = answers.get().getDislikeProfileIdList();
            list.add(dislikesDto.getProfileId());
            answers.get().setDislikeProfileIdList(list);
            answers.get().setNumberOfDislikes(answers.get().getNumberOfDislikes()+1);

            answersRepo.save(answers.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> addEmojis(EmojisDto emojisDto) {
        Optional<Answers> answers=answersRepo.findById(emojisDto.getQuestionOrAnswerId());
        if(answers.isPresent()) {

            List<String> listOfId=answers.get().getEmojiProfileIdList();
            List<String> listOfEmoji=answers.get().getEmojiValue();

            listOfId.add(emojisDto.getProfileId());
            answers.get().setDislikeProfileIdList(listOfId);

            listOfEmoji.add(emojisDto.getEmojisValue());
            answers.get().setEmojiValue(listOfEmoji);

            answersRepo.save(answers.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> addLikes(LikesDto likesDto) {
        Optional<Answers> answers=answersRepo.findById(likesDto.getQuestionOrAnswerId());
        if(answers.isPresent()) {

            List<String> list=answers.get().getLikeProfileIdList();
            list.add(likesDto.getProfileId());
            answers.get().setLikeProfileIdList(list);
            answers.get().setNumberOfLikes(answers.get().getNumberOfLikes()+1);

            answersRepo.save(answers.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> setAnswerStatus(String answerId, Boolean status) {
        Optional<Answers> answers = answersRepo.findById(answerId);
        if (answers.isPresent()) {
            Optional<Questions> questions = questionsRepo.findById(answers.get().getQuestionId());
            if (questions.isPresent()) {
                if (questions.get().getProfileWhereAskedType().equals("business") ||
                        questions.get().getProfileWhereAskedType().equals("private") ||
                        questions.get().getProfileWhereAskedType().equals("organization")) {

                    answers.get().setAnswerStatus(status);
                }
                return new ResponseEntity<String>(HttpStatus.OK);

            } else {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Boolean> getAnswerStatus(String answerId) {
        Optional<Answers> answers = answersRepo.findById(answerId);
        if (answers.isPresent()) {
            Optional<Questions> questions = questionsRepo.findById(answers.get().getQuestionId());
            if (questions.isPresent()) {

                return new ResponseEntity<Boolean>(answers.get().getAnswerStatus(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<String> addAnswer(Answers answers) {

        Optional<Questions> questions=questionsRepo.findById(answers.getQuestionId());
        if(questions.isPresent()){

        List list=questions.get().getAnswerIdList();
        list.add(answers.getAnswerId());
        questions.get().setAnswerIdList(list);
        questionsRepo.save(questions.get());

        String profileType=questions.get().getProfileWhereAskedType();
        if(profileType.equals("public")){
            answers.setAnswerStatus(true);
        }

        // set questions of private or business profiles to false status by default
        if(profileType.equals("business") || profileType.equals("private") || profileType.equals("organization")) {
            answers.setAnswerStatus(false);
        }
        answersRepo.save(answers);
        return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> getProfileIdByAnswerId(String answerId) {
        Optional<Answers> answers = answersRepo.findById(answerId);
        if (answers.isPresent()) {
            String questionId=answers.get().getQuestionId();
            return questionsServices.getProfileIdByQuestionId(questionId);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
}
