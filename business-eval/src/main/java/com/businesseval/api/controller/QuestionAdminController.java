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

import com.businesseval.api.modelin.TextRequest;
import com.businesseval.domain.model.Question;
import com.businesseval.domain.repository.QuestionRepository;
import com.businesseval.domain.service.QuestionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/questions")
public class QuestionAdminController {
	private QuestionService questionService;
	private QuestionRepository questionRepository;
	
	@PostMapping
	public Question save(@Valid @RequestBody Question question) {
		return questionService.save(question);
	}
	
	@PutMapping("/{questionId}")
	public Question edit(@PathVariable Long questionId ,@Valid @RequestBody Question question) {
		return questionService.edit(questionId, question);
	}
	
	@PutMapping("/position/{questionId}")
	public Question editPosition(@PathVariable Long questionId ,@Valid @RequestBody Question question) {
		return questionService.editPosition(questionId, question);
	}
	
	@GetMapping("/{questionId}")
	public Question search(@PathVariable Long questionId) {
		return questionService.search(questionId);
	}
	
	@GetMapping("/description/contains")
	public List<Question> searchDescriptionContains(@RequestBody TextRequest questionDescription){
		return questionRepository.findByDescriptionContains(questionDescription.getText());
	}

	@GetMapping
	public List<Question> listAll(){
		return questionRepository.findAll();
	}
	
	@DeleteMapping("/{questionId}")
	public ResponseEntity<Void> delete(@PathVariable Long questionId){
		return questionService.delete(questionId);
	}
}