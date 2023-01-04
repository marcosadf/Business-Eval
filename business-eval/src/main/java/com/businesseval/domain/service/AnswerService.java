package com.businesseval.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.exception.BusinessException;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.model.Answer;
import com.businesseval.domain.model.User;
import com.businesseval.domain.repository.AnswerRepository;

@Service
public class AnswerService {
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private BusinessUserService businessUserService;
	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	public Answer save(Answer answer) {
		if(answer.getId() != null || businessUserService.search(answer.getBusinessUser().getId())
			.getAnswers().stream()
			.anyMatch(a -> a.getQuestion().getId() == answer.getQuestion().getId())
			) {
			throw new BusinessException(messageSource.getMessage("answer.exist", null, LocaleContextHolder.getLocale()));
		}
		return answerRepository.save(answer);
	}
	
	public Answer editValue(Long answerId, Answer answer) {
		Answer researchedAnswer = search(answerId);
		researchedAnswer.setValue(answer.getValue());
		return answerRepository.save(researchedAnswer);	
	}
	
	public ResponseEntity<Void> delete(Long answerId) {
		search(answerId);
		answerRepository.deleteById(answerId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	public Answer search(Long answerId) {
		Optional<Answer> answer = answerRepository.findById(answerId);
		if(answer.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("answer.not.found", null, LocaleContextHolder.getLocale()));
		}
		return answer.get();
	}
	
	// ----------------------------------------User Default----------------------------------------
	public Answer saveSelf(Answer answer, User user) {
		if(!user.getBusinessUsers().stream().anyMatch(b -> b.getId() == answer.getBusinessUser().getId())) {
			throw new EntityNotFoundException(messageSource.getMessage("businessUser.not.found", null, LocaleContextHolder.getLocale()));
		}
		return save(answer);
	}
	
	
	public Answer searchCreated(Long answerId, User user) {
		if(user.getBusinessUsers().isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("businessUser.not.found", null, LocaleContextHolder.getLocale()));
		}
		Optional<Optional<Answer>> answer = user.getBusinessUsers().stream()
				.filter(b -> b.getAnswers().stream().anyMatch(a -> a.getId() == answerId))
				.map(b -> b.getAnswers().stream().filter(a -> a.getId() == answerId).findFirst()).findFirst();
		if(answer.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		if(answer.get().isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return answer.get().get();
	}
	
	public Answer editCreatedValue(Long answerId, Answer answer, User user) {
		Answer researchedAnswer = searchCreated(answerId, user);
		researchedAnswer.setValue(answer.getValue());
		return answerRepository.save(researchedAnswer);	
	}
	
	public ResponseEntity<Void> deleteCreated(Long answerId, User user) {
		searchCreated(answerId, user);
		answerRepository.deleteById(answerId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
}
