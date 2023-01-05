package com.businesseval.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.assembler.QuestionAssembler;
import com.businesseval.api.modelin.TextRequest;
import com.businesseval.api.modelout.QuestionOut;
import com.businesseval.domain.repository.QuestionRepository;
import com.businesseval.domain.service.QuestionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {
	private QuestionService questionService;
	private QuestionRepository questionRepository;
	private QuestionAssembler questionAssembler;
	
	@GetMapping("/{questionId}")
	public QuestionOut search(@PathVariable Long questionId) {
		return questionAssembler.toOut(questionService.search(questionId));
	}
	
	@GetMapping("/description/contains")
	public List<QuestionOut> searchDescriptionContains(@RequestBody TextRequest questionName){
		return questionAssembler.toCollectionOut(questionRepository.findByDescriptionContains(questionName.getText()));
	}

	@GetMapping
	public List<QuestionOut> listAll(){
		return questionAssembler.toCollectionOut(questionRepository.findAll());
	}

}