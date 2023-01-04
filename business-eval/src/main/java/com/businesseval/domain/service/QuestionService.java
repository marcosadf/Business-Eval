package com.businesseval.domain.service;

import java.util.List;
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
import com.businesseval.domain.model.Question;
import com.businesseval.domain.repository.QuestionRepository;

@Service
public class QuestionService {
	@Autowired
	private QuestionRepository questionRepository;
	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	public Question save(Question question) {
		if(question.getId() != null) {
			Optional<Question> researchedQuestion = questionRepository.findById(question.getId());
			if(researchedQuestion.isPresent()) {
				if(researchedQuestion.get().getDescription().equals(question.getDescription())) {
					if(!questionRepository.findByDescription(question.getDescription()).isEmpty()) {
						throw new BusinessException(messageSource.getMessage("description.question.exist", null, LocaleContextHolder.getLocale()));
					}
				}
			}
			else {
				throw new EntityNotFoundException(messageSource.getMessage("add.question.not.found", null, LocaleContextHolder.getLocale()));
			}
		}else if(!questionRepository.findByDescription(question.getDescription()).isEmpty()) {
			throw new BusinessException(messageSource.getMessage("description.question.exist", null, LocaleContextHolder.getLocale()));
		}else {
			long numCategories = questionRepository.count();
			question.setPosition(numCategories + 1);
		}
		return questionRepository.save(question);
	}
	
	public Question edit(Long questionId, Question question) {
		Question researchedQuestion = search(questionId);
		if(researchedQuestion.getPosition() != question.getPosition()) {
			throw new EntityNotFoundException(messageSource.getMessage("position.question.not.edit", null, LocaleContextHolder.getLocale()));
		}
		question.setId(questionId);
		return save(question);	
	}
	
	public Question editPosition(Long questionId, Question question) {
		Question researchedQuestion = search(questionId);
		if(researchedQuestion.getPosition() != question.getPosition()) {
			if(question.getPosition() > questionRepository.count() || question.getPosition() < 0) {
				throw new EntityNotFoundException(messageSource.getMessage("position.question.not.valid", null, LocaleContextHolder.getLocale()));
			}
			List<Question> categories = questionRepository.findAll();
			categories.stream().forEach(c -> {
				if(!c.equals(researchedQuestion)) {
					if(c.getPosition() < researchedQuestion.getPosition() && c.getPosition() >= question.getPosition()) {
						c.setPosition(c.getPosition() + 1);
						questionRepository.save(c);
					}else if(c.getPosition() > researchedQuestion.getPosition() && c.getPosition() <= question.getPosition()) {
						c.setPosition(c.getPosition() - 1);
						questionRepository.save(c);
					}
				}
			});
		}
		researchedQuestion.setPosition(question.getPosition());
		return questionRepository.save(researchedQuestion);	
	}
	
	public ResponseEntity<Void> delete(Long questionId) {
		search(questionId);
		questionRepository.deleteById(questionId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	public Question search(Long questionId) {
		Optional<Question> question = questionRepository.findById(questionId);
		if(question.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("question.not.found", null, LocaleContextHolder.getLocale()));
		}
		return question.get();
	}
}
