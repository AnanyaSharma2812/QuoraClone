package com.example.questionsAndAnswersMicroservice.services.servicesImpl;

import com.example.questionsAndAnswersMicroservice.document.Category;
import com.example.questionsAndAnswersMicroservice.dto.*;
import com.example.questionsAndAnswersMicroservice.document.Questions;
import com.example.questionsAndAnswersMicroservice.repository.CategoryRepo;
import com.example.questionsAndAnswersMicroservice.repository.QuestionsRepo;
import com.example.questionsAndAnswersMicroservice.services.CategoryServices;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionsServicesImpl implements QuestionsServices {

    @Autowired
    QuestionsRepo questionsRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    CategoryServices categoryServices;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

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
    public Page<Questions> getAllQuestions(Integer pageNumber, Integer pageSize, String sortBy) {
        return questionsRepo.findAll(PageRequest.of(pageNumber, pageSize,Sort.by(sortBy)));
    }

    @Override
    public Page<Questions> getQuestionsOfSelectedCategories(GetQuestionsOfSelectedCategoriesRequestDto getQuestionsRequestDto) {

        Pageable paging=PageRequest.of(getQuestionsRequestDto.getPageNumber(),getQuestionsRequestDto.getPageSize(),Sort.by(getQuestionsRequestDto.getSortBy()));
        List<String> questionIdList=new ArrayList<>();

        Iterator it=getQuestionsRequestDto.getCategoryIdList().iterator();
        while(it.hasNext()){
            Optional<Category> category=categoryRepo.findByCategoryIdIgnoreCase(String.valueOf(it.next()));
            if(category.isPresent()){
                List<String> list1=category.get().getQuestionIdList();
                Iterator itr=list1.iterator();
                while(itr.hasNext()){
                    if(questionsRepo.findById(String.valueOf(itr.next())).isPresent()) {

                        questionIdList.add(String.valueOf(itr.next()));
                    }
                }
            }
        }

        return  questionsRepo.findByQuestionIdIn(questionIdList,paging);

    }

    @Override
    public ResponseEntity<String> addQuestion(Questions questions) throws JsonProcessingException {

        SearchDto searchDto=new SearchDto();

        questions.setIsThreadOpen(true);
        questions.setApprovedAnswerId("");

        //null check for tagging
        if(null==questions.getProfileWhereAskedType() || null==questions.getProfileWhereAskedId() || null==questions.getProfileWhereAskedName())
        {
            questions.setProfileWhereAskedId("");
            questions.setProfileWhereAskedName("");
            questions.setProfileWhereAskedType("");
        }


        //to send details to notification microservice
        QuestionNotifResponseDto questionNotifResponseDto=new QuestionNotifResponseDto();
        questionNotifResponseDto.setAskerId(questions.getAskerProfileId());
        questionNotifResponseDto.setAskerUserName(questions.getAskerProfileName());

        questionNotifResponseDto.setTaggedProfileId(questions.getProfileWhereAskedId());
        questionNotifResponseDto.setTaggedProfileName(questions.getProfileWhereAskedName());
        questionNotifResponseDto.setTaggedProfileType(questions.getProfileWhereAskedType());

        questionNotifResponseDto.setCategory(questions.getCategoryId());
        questionNotifResponseDto.setIsApproved(false);

        // call profile microservice to add points
        String uri="http://172.16.20.119:8080/profile/addPoints/10/"+questions.getAskerProfileId();
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.put(uri,null);

        // set questions of public profiles to true status by default
        String profileType=questions.getProfileWhereAskedType();
        if(profileType.equals("public")){
            questions.setQuestionStatus(true);
        }

        // set questions of private or business profiles to false status by default
        if(profileType.equals("business") || profileType.equals("private") || profileType.equals("organization")) {
            questions.setQuestionStatus(false);
        }

        //kafka producer
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate.send("newQues",objectMapper.writeValueAsString(questionNotifResponseDto));



        String questionId=questionsRepo.save(questions).getQuestionId();

        CategoryDto categoryDto=new CategoryDto();

        categoryDto.setCategoryId(questions.getCategoryId());
        categoryDto.setCategoryName(questions.getCategoryName());
        categoryDto.setQuestionId(questionId);

        categoryServices.addQuestionToACategory(questions.getCategoryId(),questionId);

        //to copy data to search
        BeanUtils.copyProperties(questions,searchDto);
        searchDto.setValueType("Q");

        kafkaTemplate.send("addToSearch",objectMapper.writeValueAsString(searchDto));

        return new ResponseEntity<String>(questionsRepo.save(questions).getQuestionId(),HttpStatus.OK);
    }

    @Override
    //for moderators
    public ResponseEntity<String> setQuestionStatus(String questionId, Boolean status) throws JsonProcessingException {

        Optional<Questions> questions=questionsRepo.findById(questionId);
        if(questions.isPresent()) {

            if(questions.get().getProfileWhereAskedType().equals("business") ||
                    questions.get().getProfileWhereAskedType().equals("private") ||
                    questions.get().getProfileWhereAskedType().equals("organization")) {

                questions.get().setQuestionStatus(status);

                //to send details to notification microservice
                QuestionNotifResponseDto questionNotifResponseDto=new QuestionNotifResponseDto();
                questionNotifResponseDto.setAskerId(questions.get().getAskerProfileId());
                questionNotifResponseDto.setAskerUserName(questions.get().getAskerProfileName());

                questionNotifResponseDto.setTaggedProfileId(questions.get().getProfileWhereAskedId());
                questionNotifResponseDto.setTaggedProfileName(questions.get().getProfileWhereAskedName());
                questionNotifResponseDto.setTaggedProfileType(questions.get().getProfileWhereAskedType());

                questionNotifResponseDto.setCategory(questions.get().getCategoryId());

                questionNotifResponseDto.setIsApproved(questions.get().getQuestionStatus());

                SearchDto searchDto=new SearchDto();
                //to copy data to search
                BeanUtils.copyProperties(questions,searchDto);
                searchDto.setValueType("Q");


                ObjectMapper objectMapper = new ObjectMapper();
                kafkaTemplate.send("quesApproval",objectMapper.writeValueAsString(questionNotifResponseDto));
                kafkaTemplate.send("addToSearch",objectMapper.writeValueAsString(searchDto));

            }
            return new ResponseEntity<String>(HttpStatus.OK);

        }

        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> addDislikes(DislikesDto dislikesDto) throws JsonProcessingException {

        Optional<Questions> questions=questionsRepo.findById(dislikesDto.getQuestionOrAnswerId());
        if(questions.isPresent()) {

            dislikesDto.setAskerOrAnswererId(questions.get().getAskerProfileId());

        List<String> list=questions.get().getDislikeProfileIdList();
        list.add(dislikesDto.getProfileId());
        questions.get().setDislikeProfileIdList(list);
        questions.get().setNumberOfDislikes(questions.get().getNumberOfDislikes()+1);

        questionsRepo.save(questions.get());

        //send to notification
            ReactionDto reactionDto=new ReactionDto();
            reactionDto.setReactedUserId(dislikesDto.getProfileId());
            reactionDto.setReactedUserName(dislikesDto.getProfileName());
            reactionDto.setReactedUserName(dislikesDto.getAskerOrAnswererId());
            reactionDto.setReactionType("Dislike");
            reactionDto.setPostType("Question");

            SearchDto searchDto=new SearchDto();
            //to copy data to search
            BeanUtils.copyProperties(questions,searchDto);
            searchDto.setValueType("Q");


            //kafka producer
            ObjectMapper objectMapper=new ObjectMapper();
            kafkaTemplate.send("reaction",objectMapper.writeValueAsString(reactionDto));
            kafkaTemplate.send("addToSearch",objectMapper.writeValueAsString(searchDto));

            // call profile microservice to add points
            final String uri="http://172.16.20.119:8080/profile/addPoints/-1/"+dislikesDto.getAskerOrAnswererId();
            RestTemplate restTemplate=new RestTemplate();
            restTemplate.put(uri,null);

        return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> addEmojis(EmojisDto emojisDto) throws JsonProcessingException {

        Optional<Questions> questions=questionsRepo.findById(emojisDto.getQuestionOrAnswerId());
        if(questions.isPresent()) {

            emojisDto.setAskerOrAnswererId(questions.get().getAskerProfileId());

            List<String> listOfId=questions.get().getEmojisProfileIdList();
            List<String> listOfEmoji=questions.get().getEmojisValue();

            listOfId.add(emojisDto.getProfileId());
            questions.get().setDislikeProfileIdList(listOfId);

            listOfEmoji.add(emojisDto.getEmojisValue());
            questions.get().setEmojisValue(listOfEmoji);

            //send to notification
            ReactionDto reactionDto=new ReactionDto();
            reactionDto.setReactedUserId(emojisDto.getProfileId());
            reactionDto.setReactedUserName(emojisDto.getProfileName());
            reactionDto.setReactedUserName(emojisDto.getAskerOrAnswererId());
            reactionDto.setReactionType("Emoji");
            reactionDto.setPostType("Question");

            //kafka producer
            ObjectMapper objectMapper=new ObjectMapper();
            kafkaTemplate.send("reaction",objectMapper.writeValueAsString(reactionDto));

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
    public ResponseEntity<String> addLikes(LikesDto likesDto) throws JsonProcessingException {
        Optional<Questions> questions=questionsRepo.findById(likesDto.getQuestionOrAnswerId());
        if(questions.isPresent()) {

            likesDto.setAskerOrAnswererId(questions.get().getAskerProfileId());

            List<String> list=questions.get().getLikeProfileIdList();
            list.add(likesDto.getProfileId());
            questions.get().setLikeProfileIdList(list);
            questions.get().setNumberOfLikes(questions.get().getNumberOfLikes()+1);

            //send to notification
            ReactionDto reactionDto=new ReactionDto();
            reactionDto.setReactedUserId(likesDto.getProfileId());
            reactionDto.setReactedUserName(likesDto.getProfileName());
            reactionDto.setReactedUserName(likesDto.getAskerOrAnswererId());
            reactionDto.setReactionType("like");
            reactionDto.setPostType("Question");

            SearchDto searchDto=new SearchDto();
            //to copy data to search
            BeanUtils.copyProperties(questions,searchDto);
            searchDto.setValueType("Q");


            //kafka producer
            ObjectMapper objectMapper=new ObjectMapper();
            kafkaTemplate.send("reaction",objectMapper.writeValueAsString(reactionDto));
            kafkaTemplate.send("addToSearch",objectMapper.writeValueAsString(searchDto));

            // call profile microservice to add points
            final String uri="http://172.16.20.119:8080/profile/addPoints/1/"+likesDto.getAskerOrAnswererId();
            RestTemplate restTemplate=new RestTemplate();
            restTemplate.put(uri,null);

            questionsRepo.save(questions.get());

            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> setApprovedAnswer(SetApprovedAnswerRequestDto setApprovedAnswerRequestDto) throws JsonProcessingException {
        Optional<Questions> questions=questionsRepo.findById(setApprovedAnswerRequestDto.getQuestionId());
        if(questions.isPresent()) {

            //send to notification
            questions.get().setApprovedAnswerId(setApprovedAnswerRequestDto.getAnswerId());
            questions.get().setApprovedAnswer(setApprovedAnswerRequestDto.getAnswer());
            questions.get().setApprovedAnswererId(setApprovedAnswerRequestDto.getProfileId());
            questions.get().setApprovedAnswererProfile(setApprovedAnswerRequestDto.getProfileName());

            AnswersNotifResponseDto answersNotifResponseDto=new AnswersNotifResponseDto();
            answersNotifResponseDto.setAnswerUserId(questions.get().getApprovedAnswererId());
            answersNotifResponseDto.setQuestionAskerName(questions.get().getAskerProfileName());

            questions.get().setIsThreadOpen(false);

            SearchDto searchDto=new SearchDto();
            //to copy data to search
            BeanUtils.copyProperties(questions,searchDto);
            searchDto.setValueType("Q");


            // call profile microservice to add points
            final String uri="http://172.16.20.119:8080/profile/addPoints/5/"+setApprovedAnswerRequestDto.getProfileId();
            RestTemplate restTemplate=new RestTemplate();
            restTemplate.put(uri,null);

            questionsRepo.save(questions.get());

        //kafka producer
            ObjectMapper objectMapper=new ObjectMapper();
            kafkaTemplate.send("threadClosed",objectMapper.writeValueAsString(answersNotifResponseDto));
            kafkaTemplate.send("addToSearch",objectMapper.writeValueAsString(searchDto));

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

    @Override
    public Page<Questions> getQuestionsByUserId(String userId,Integer pageNumber,Integer pageSize,String sortBy) {

        return questionsRepo.findAllByAskerProfileId(userId,PageRequest.of(pageNumber, pageSize,Sort.by(sortBy)));

        }


}

