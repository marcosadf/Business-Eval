package com.businesseval.domain.service;

import java.util.Collections;
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
	@Autowired
	private CategoryService categoryService;
	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	public Question save(Question question) {
		if(question.getId() != null) {
			Optional<Question> researchedQuestion = questionRepository.findById(question.getId());
			if(researchedQuestion.isPresent()) {
				if(!researchedQuestion.get().getDescription().equals(question.getDescription())) {
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
			long numQuestions = categoryService.search(question.getCategory().getId()).getQuestions().size();
			System.out.println(numQuestions);
			question.setPosition(numQuestions + 1);
		}
		return questionRepository.save(question);
	}
	
	public Question edit(Long questionId, Question question) {
		Question researchedQuestion = search(questionId);
		if(researchedQuestion.getPosition() != question.getPosition()) {
			throw new BusinessException(messageSource.getMessage("position.question.not.edit", null, LocaleContextHolder.getLocale()));
		}
		question.setId(questionId);
		return save(question);	
	}
	
	public Question editPosition(Long questionId, Question question) {
		Question researchedQuestion = search(questionId);
		List<Question> questions = researchedQuestion.getCategory().getQuestions();
		if(researchedQuestion.getPosition() != question.getPosition()) {
			if(question.getPosition() > questions.size() || question.getPosition() < 0) {
				throw new EntityNotFoundException(messageSource.getMessage("position.question.not.valid", null, LocaleContextHolder.getLocale()));
			}
			questions.stream().forEach(q -> {
				if(!q.equals(researchedQuestion)) {
					if(q.getPosition() < researchedQuestion.getPosition() && q.getPosition() >= question.getPosition()) {
						q.setPosition(q.getPosition() + 1);
						questionRepository.save(q);
					}else if(q.getPosition() > researchedQuestion.getPosition() && q.getPosition() <= question.getPosition()) {
						q.setPosition(q.getPosition() - 1);
						questionRepository.save(q);
					}
				}
			});
		}
		researchedQuestion.setPosition(question.getPosition());
		return questionRepository.save(researchedQuestion);	
	}
	
	public ResponseEntity<Void> delete(Long questionId) {
		Long categoryid = search(questionId).getCategory().getId();
		questionRepository.deleteById(questionId);
		sortPosition(categoryid);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	private void sortPosition(Long categoryId) {
		List<Question> questions = categoryService.search(categoryId).getQuestions();
		Collections.sort(questions,(q1, q2) -> {
			return (int) (q1.getPosition() - q2.getPosition());
		});
		for (int i = 0; i < questions.size(); i++) {
			Question q = questions.get(i);
			q.setPosition((long) (i+1));
			questionRepository.save(q);
		}
	}
	
	public Question search(Long questionId) {
		Optional<Question> question = questionRepository.findById(questionId);
		if(question.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("question.not.found", null, LocaleContextHolder.getLocale()));
		}
		return question.get();
	}
}
