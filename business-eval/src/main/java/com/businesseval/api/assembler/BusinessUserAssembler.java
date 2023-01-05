package com.businesseval.api.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.businesseval.api.modelin.BusinessUserIn;
import com.businesseval.api.modelout.BusinessUserOut;
import com.businesseval.domain.model.Answer;
import com.businesseval.domain.model.BusinessUser;

@Component
public class BusinessUserAssembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public BusinessUserAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.modelMapper.createTypeMap(BusinessUser.class, BusinessUserOut.class)
		.<List<Answer>>addMapping(src -> src.getAnswers(), (src, value) -> {
			if(value != null) {
				src.setAnswers((new AnswerAssembler(modelMapper)).toCollectionOut(value));
			}else {
				src.setAnswers(new ArrayList<>());
			}
		});
	}
	
	public BusinessUser toIn(BusinessUserIn businessUser) {
		return modelMapper.map(businessUser, BusinessUser.class);
	}
	
	public BusinessUserOut toOut(BusinessUser businessUser) {
		return modelMapper.map(businessUser, BusinessUserOut.class);
	}

	public List<BusinessUserOut> toCollectionOut(List<BusinessUser> listBusinessUsers) {
		return listBusinessUsers.stream().map(this::toOut).collect(Collectors.toList());
	}
}
