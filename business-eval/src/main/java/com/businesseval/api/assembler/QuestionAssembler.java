package com.businesseval.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.businesseval.api.modelin.QuestionIn;
import com.businesseval.api.modelout.QuestionOut;
import com.businesseval.domain.model.Question;

@Component
public class QuestionAssembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public QuestionAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public Question toIn(QuestionIn question) {
		return modelMapper.map(question, Question.class);
	}
	
	public QuestionOut toOut(Question question) {
		return modelMapper.map(question, QuestionOut.class);
	}

	public List<QuestionOut> toCollectionOut(List<Question> listQuetion) {
		return listQuetion.stream().map(this::toOut).collect(Collectors.toList());
	}
}
