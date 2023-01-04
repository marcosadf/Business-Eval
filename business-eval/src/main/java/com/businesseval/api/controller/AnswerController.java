package com.businesseval.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.common.ExtractUserJWT;
import com.businesseval.domain.model.Answer;
import com.businesseval.domain.service.AnswerService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/answers")
public class AnswerController {
	private AnswerService answerService;
	
	@PostMapping
	public Answer saveSelf(@Valid @PathVariable Answer answer, @RequestHeader HttpHeaders headers) {
		return answerService.saveSelf(answer, ExtractUserJWT.extract(headers));
	}
	
	@GetMapping("/{answerId}")
	public Answer searchCreated(@PathVariable Long answerId, @RequestHeader HttpHeaders headers) {
		return answerService.searchCreated(answerId, ExtractUserJWT.extract(headers));
	}
	
	@PutMapping("/{answerId}")
	public Answer editCreatedValue(@PathVariable Long answerId ,@Valid @RequestBody Answer answer, @RequestHeader HttpHeaders headers) {
		return answerService.editCreatedValue(answerId, answer, ExtractUserJWT.extract(headers));
	}
	
	@DeleteMapping("/{answerId}")
	public ResponseEntity<Void> deleteCreated(@PathVariable Long answerId, @RequestHeader HttpHeaders headers) {
		return answerService.deleteCreated(answerId, ExtractUserJWT.extract(headers));
	}
	

}