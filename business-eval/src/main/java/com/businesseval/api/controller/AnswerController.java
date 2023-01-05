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

import com.businesseval.api.assembler.AnswerAssembler;
import com.businesseval.api.modelin.AnswerIn;
import com.businesseval.api.modelout.AnswerOut;
import com.businesseval.common.ExtractUserJWT;
import com.businesseval.domain.service.AnswerService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/answers")
public class AnswerController {
	private AnswerService answerService;
	private AnswerAssembler answerAssembler;
	
	@PostMapping
	public AnswerOut saveSelf(@Valid @RequestBody AnswerIn answer, @RequestHeader HttpHeaders headers) {
		return answerAssembler.toOut(answerService.saveSelf(answerAssembler.toIn(answer), ExtractUserJWT.extract(headers)));
	}
	
	@GetMapping("/{answerId}")
	public AnswerOut searchCreated(@PathVariable Long answerId, @RequestHeader HttpHeaders headers) {
		return answerAssembler.toOut(answerService.searchCreated(answerId, ExtractUserJWT.extract(headers)));
	}
	
	@PutMapping("/{answerId}")
	public AnswerOut editCreatedValue(@PathVariable Long answerId ,@Valid @RequestBody AnswerIn answer, @RequestHeader HttpHeaders headers) {
		return answerAssembler.toOut(answerService.editCreatedValue(answerId, answerAssembler.toIn(answer), ExtractUserJWT.extract(headers)));
	}
	
	@DeleteMapping("/{answerId}")
	public ResponseEntity<Void> deleteCreated(@PathVariable Long answerId, @RequestHeader HttpHeaders headers) {
		return answerService.deleteCreated(answerId, ExtractUserJWT.extract(headers));
	}
	

}