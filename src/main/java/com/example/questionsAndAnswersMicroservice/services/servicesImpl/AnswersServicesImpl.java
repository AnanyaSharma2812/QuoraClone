package com.example.questionsAndAnswersMicroservice.services.servicesImpl;

import com.example.questionsAndAnswersMicroservice.dto.*;
import com.example.questionsAndAnswersMicroservice.document.Answers;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.example.questionsAndAnswersMicroservice.repository.AnswersRepo;
import com.example.questionsAndAnswersMicroservice.repository.QuestionsRepo;
import com.example.questionsAndAnswersMicroservice.services.AnswersServices;
import com.example.questionsAndAnswersMicroservice.services.QuestionsServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;


    @Override
    public ResponseEntity<String> addDislikes(DislikesDto dislikesDto) throws JsonProcessingException {

        Optional<Answers> answers = answersRepo.findById(dislikesDto.getQuestionOrAnswerId());
        if (answers.isPresent()) {

            dislikesDto.setAskerOrAnswererId(answers.get().getProfileIdOfAnswerer());

            List<String> list = answers.get().getDislikeProfileIdList();
            list.add(dislikesDto.getProfileId());
            answers.get().setDislikeProfileIdList(list);
            answers.get().setNumberOfDislikes(answers.get().getNumberOfDislikes()+1);

            //send to notification
            ReactionDto reactionDto=new ReactionDto();
            reactionDto.setReactedUserId(dislikesDto.getProfileId());
            reactionDto.setReactedUserName(dislikesDto.getProfileName());
            reactionDto.setReactedUserName(dislikesDto.getAskerOrAnswererId());
            reactionDto.setReactionType("Dislike");
            reactionDto.setPostType("Answer");

            SearchDto searchDto=new SearchDto();
            //to copy data to search
            BeanUtils.copyProperties(answers,searchDto);
            searchDto.setValueType("Q");


            //kafka producer
            ObjectMapper objectMapper=new ObjectMapper();
            kafkaTemplate.send("reaction",objectMapper.writeValueAsString(reactionDto));

            // call profile microservice to add points
            final String uri="http://172.16.20.119:8080/profile/addPoints/-1/"+ dislikesDto.getAskerOrAnswererId();
            RestTemplate restTemplate=new RestTemplate();
            restTemplate.put(uri,null);

            answersRepo.save(answers.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> addEmojis(EmojisDto emojisDto) throws JsonProcessingException {
        Optional<Answers> answers=answersRepo.findById(emojisDto.getQuestionOrAnswerId());
        if(answers.isPresent()) {

            emojisDto.setAskerOrAnswererId(answers.get().getProfileIdOfAnswerer());

            List<String> listOfId=answers.get().getEmojiProfileIdList();
            List<String> listOfEmoji=answers.get().getEmojiValue();

            listOfId.add(emojisDto.getProfileId());
            answers.get().setDislikeProfileIdList(listOfId);

            listOfEmoji.add(emojisDto.getEmojisValue());
            answers.get().setEmojiValue(listOfEmoji);

            //send to notification
            ReactionDto reactionDto=new ReactionDto();
            reactionDto.setReactedUserId(emojisDto.getProfileId());
            reactionDto.setReactedUserName(emojisDto.getProfileName());
            reactionDto.setReactedUserName(emojisDto.getAskerOrAnswererId());
            reactionDto.setReactionType("Emoji");
            reactionDto.setPostType("Answer");

            //kafka producer
            ObjectMapper objectMapper=new ObjectMapper();
            kafkaTemplate.send("reaction",objectMapper.writeValueAsString(reactionDto));

            answersRepo.save(answers.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> addLikes(LikesDto likesDto) throws JsonProcessingException {
        Optional<Answers> answers=answersRepo.findById(likesDto.getQuestionOrAnswerId());
        if(answers.isPresent()) {

            likesDto.setAskerOrAnswererId(answers.get().getProfileIdOfAnswerer());

            List<String> list=answers.get().getLikeProfileIdList();
            list.add(likesDto.getProfileId());
            answers.get().setLikeProfileIdList(list);
            answers.get().setNumberOfLikes(answers.get().getNumberOfLikes()+1);

            //send to notification
            ReactionDto reactionDto=new ReactionDto();
            reactionDto.setReactedUserId(likesDto.getProfileId());
            reactionDto.setReactedUserName(likesDto.getProfileName());
            reactionDto.setReactedUserName(likesDto.getAskerOrAnswererId());
            reactionDto.setReactionType("like");
            reactionDto.setPostType("Answer");

            //kafka producer
            ObjectMapper objectMapper=new ObjectMapper();
            kafkaTemplate.send("reaction",objectMapper.writeValueAsString(reactionDto));

            // call profile microservice to add points
            final String uri="http://172.16.20.119:8080/profile/addPoints/1/"+likesDto.getAskerOrAnswererId();
            RestTemplate restTemplate=new RestTemplate();
            restTemplate.put(uri,null);

            answersRepo.save(answers.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> setAnswerStatus(String answerId, Boolean status) throws JsonProcessingException {
        Optional<Answers> answers = answersRepo.findById(answerId);
        if (answers.isPresent()) {
            Optional<Questions> questions = questionsRepo.findById(answers.get().getQuestionId());
            if (questions.isPresent()) {
                if (questions.get().getProfileWhereAskedType().equals("business") ||
                        questions.get().getProfileWhereAskedType().equals("private") ||
                        questions.get().getProfileWhereAskedType().equals("organization")) {

                    answers.get().setAnswerStatus(status);

                    //send to notification
                    AnswersNotifResponseDto answersNotifResponseDto=new AnswersNotifResponseDto();
                    answersNotifResponseDto.setQuestionAskerId(answers.get().getQuestionAskerId());
                    answersNotifResponseDto.setQuestionAskerName(answers.get().getQuestionAskerName());

                    answersNotifResponseDto.setTaggedProfileId(answers.get().getProfileWhereAskedId());
                    answersNotifResponseDto.setTaggedProfileName(answers.get().getProfileWhereAskedName());
                    answersNotifResponseDto.setTaggedProfileType(answers.get().getProfileWhereAskedType());

                    answersNotifResponseDto.setAnswerUserId(answers.get().getProfileIdOfAnswerer());
                    answersNotifResponseDto.setAnswerUserName(answers.get().getProfileNameOfAnswerer());

                    answersNotifResponseDto.setCategory(questions.get().getCategoryId());

                    answersNotifResponseDto.setIsApproved(answers.get().getAnswerStatus());

                    //kafka producer
                    ObjectMapper objectMapper = new ObjectMapper();
                    kafkaTemplate.send("ansApproval",objectMapper.writeValueAsString(answersNotifResponseDto));

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
    public Page<Answers> getAllAnswersOfAQuestion(Integer pageNumber, Integer pageSize, String sortBy, String questionId) {

        Pageable paging=PageRequest.of(pageNumber,pageSize,Sort.by(sortBy));
        return answersRepo.findAllByQuestionId(questionId,paging);
    }

    @Override
    public ResponseEntity<String> addAnswer(Answers answers) throws JsonProcessingException {

        Optional<Questions> questions=questionsRepo.findById(answers.getQuestionId());
        if(questions.isPresent()){

        List list=questions.get().getAnswerIdList();
        answers.setQuestion(questions.get().getQuestionValue());
        answers.setQuestionAskerId(questions.get().getAskerProfileId());
        answers.setQuestionAskerName(questions.get().getAskerProfileName());
        answers.setProfileWhereAskedId(questions.get().getProfileWhereAskedId());
        answers.setProfileWhereAskedName(questions.get().getProfileWhereAskedName());
        answers.setProfileWhereAskedType(questions.get().getProfileWhereAskedType());

        //send to notification
        AnswersNotifResponseDto answersNotifResponseDto=new AnswersNotifResponseDto();
        answersNotifResponseDto.setQuestionAskerId(answers.getQuestionAskerId());
        answersNotifResponseDto.setQuestionAskerName(answers.getQuestionAskerName());

        answersNotifResponseDto.setTaggedProfileId(answers.getProfileWhereAskedId());
        answersNotifResponseDto.setTaggedProfileName(answers.getProfileWhereAskedName());
        answersNotifResponseDto.setTaggedProfileType(answers.getProfileWhereAskedType());

        answersNotifResponseDto.setAnswerUserId(answers.getProfileIdOfAnswerer());
        answersNotifResponseDto.setAnswerUserName(answers.getProfileNameOfAnswerer());

        answersNotifResponseDto.setCategory(questions.get().getCategoryId());

        // call profile microservice to add points
            final String uri="http://172.16.20.119:8080/profile/addPoints/1/"+answers.getProfileIdOfAnswerer();
            RestTemplate restTemplate=new RestTemplate();
            restTemplate.put(uri,null);

        String profileType=questions.get().getProfileWhereAskedType();
        if(profileType.equals("public")){
            answers.setAnswerStatus(true);
        }

        // set questions of private or business profiles to false status by default
        if(profileType.equals("business") || profileType.equals("private") || profileType.equals("organization")) {
            answers.setAnswerStatus(false);
        }
        answersRepo.save(answers);

        list.add(answers.getAnswerId());
        questions.get().setAnswerIdList(list);
        questionsRepo.save(questions.get());

        //kafka producer
            ObjectMapper objectMapper = new ObjectMapper();
            kafkaTemplate.send("newAns",objectMapper.writeValueAsString(answersNotifResponseDto));

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

    @Override
    public Page<Answers> getAnswersByUserId(String userId, Integer pageNumber, Integer pageSize, String sortBy) {
        return answersRepo.findAllByProfileIdOfAnswerer(userId,PageRequest.of(pageNumber,pageSize,Sort.by(sortBy)));

    }
}
