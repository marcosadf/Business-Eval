package com.businesseval.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.businesseval.api.modelin.AnswerIn;
import com.businesseval.api.modelout.AnswerOut;
import com.businesseval.domain.model.Answer;

@Component
public class AnswerAssembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public AnswerAssembler(ModelMapper modelMapper) {
//		modelMapper.createTypeMap(Answer.class, AnswerOut.class)
//		.<Question>addMapping(src -> src.getQuestion(), (src, value) -> {
//			if(value != null) {
//				src.setQuestion((new QuestionAssembler(modelMapper)).toOut(value));
//			}else {
//				src.setQuestion(null);
//			}
//		});
		this.modelMapper = modelMapper;
	}
	
	public Answer toIn(AnswerIn answer) {
		return modelMapper.map(answer, Answer.class);
	}
	
	public AnswerOut toOut(Answer answer) {
		return modelMapper.map(answer, AnswerOut.class);
	}

	public List<AnswerOut> toCollectionOut(List<Answer> listAnswers) {
		return listAnswers.stream().map(this::toOut).collect(Collectors.toList());
	}
}
