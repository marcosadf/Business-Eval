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

import com.businesseval.domain.model.Answer;
import com.businesseval.domain.repository.AnswerRepository;
import com.businesseval.domain.service.AnswerService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/answers")
public class AnswerAdminController {
	private AnswerService answerService;
	private AnswerRepository answerRepository;
	
	@PostMapping
	public Answer save(@Valid @RequestBody Answer answer) {
		return answerService.save(answer);
	}
	
	@PutMapping("/{answerId}")
	public Answer editValue(@PathVariable Long answerId ,@Valid @RequestBody Answer answer) {
		return answerService.editValue(answerId, answer);
	}
	
	@GetMapping("/{answerId}")
	public Answer search(@PathVariable Long answerId) {
		return answerService.search(answerId);
	}

	@GetMapping
	public List<Answer> listAll(){
		return answerRepository.findAll();
	}
	
	@DeleteMapping("/{answerId}")
	public ResponseEntity<Void> delete(@PathVariable Long answerId){
		return answerService.delete(answerId);
	}
}