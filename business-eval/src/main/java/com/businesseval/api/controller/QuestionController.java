package com.businesseval.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.modelin.TextRequest;
import com.businesseval.domain.model.Question;
import com.businesseval.domain.repository.QuestionRepository;
import com.businesseval.domain.service.QuestionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {
	private QuestionService questionService;
	private QuestionRepository questionRepository;
	
	@GetMapping("/{questionId}")
	public Question search(@PathVariable Long questionId) {
		return questionService.search(questionId);
	}
	
	@GetMapping("/description/contains")
	public List<Question> searchDescriptionContains(@RequestBody TextRequest questionName){
		return questionRepository.findByDescriptionContains(questionName.getText());
	}

	@GetMapping
	public List<Question> listAll(){
		return questionRepository.findAll();
	}

}