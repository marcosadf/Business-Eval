package com.businesseval.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.assembler.QuestionAssembler;
import com.businesseval.api.modelin.QuestionIn;
import com.businesseval.api.modelin.TextRequest;
import com.businesseval.api.modelout.QuestionOut;
import com.businesseval.domain.repository.QuestionRepository;
import com.businesseval.domain.service.QuestionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/questions")
public class QuestionAdminController {
	private QuestionService questionService;
	private QuestionRepository questionRepository;
	private QuestionAssembler questionAssembler;
	
	@PostMapping
	public QuestionOut save(@Valid @RequestBody QuestionIn question) {
		return questionAssembler.toOut(questionService.save(questionAssembler.toIn(question)));
	}
	
	@PutMapping("/{questionId}")
	public QuestionOut edit(@PathVariable Long questionId ,@Valid @RequestBody QuestionIn question) {
		return questionAssembler.toOut(questionService.edit(questionId, questionAssembler.toIn(question)));
	}
	
	@PutMapping("/position/{questionId}")
	public QuestionOut editPosition(@PathVariable Long questionId ,@Valid @RequestBody QuestionIn question) {
		return questionAssembler.toOut(questionService.editPosition(questionId, questionAssembler.toIn(question)));
	}
	
	@GetMapping("/{questionId}")
	public QuestionOut search(@PathVariable Long questionId) {
		return questionAssembler.toOut(questionService.search(questionId));
	}
	
	@GetMapping("/description/contains")
	public List<QuestionOut> searchDescriptionContains(@RequestBody TextRequest questionDescription){
		return questionAssembler.toCollectionOut(questionRepository.findByDescriptionContains(questionDescription.getText()));
	}

	@GetMapping
	public List<QuestionOut> listAll(){
		return questionAssembler.toCollectionOut(questionRepository.findAll());
	}
	
	@DeleteMapping("/{questionId}")
	public ResponseEntity<Void> delete(@PathVariable Long questionId){
		return questionService.delete(questionId);
	}
}