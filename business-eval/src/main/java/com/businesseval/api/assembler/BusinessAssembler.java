package com.businesseval.api.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.businesseval.api.modelin.BusinessIn;
import com.businesseval.api.modelout.BusinessOut;
import com.businesseval.domain.model.Business;
import com.businesseval.domain.model.BusinessUser;

@Component
public class BusinessAssembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public BusinessAssembler(ModelMapper modelMapper) {
		modelMapper.createTypeMap(Business.class, BusinessOut.class)
		.<List<BusinessUser>>addMapping(src -> src.getBusinessUsers(), (src, value) -> {
			if(value != null) {
				src.setBusinessUsers((new BusinessUserAssembler(modelMapper)).toCollectionOut(value));
			}else {
				src.setBusinessUsers(new ArrayList<>());
			}
		});
		this.modelMapper = modelMapper;
	}
	public Business toIn(BusinessIn business) {
		return modelMapper.map(business, Business.class);
	}
	
	public BusinessOut toOut(Business business) {
		return modelMapper.map(business, BusinessOut.class);
	}

	public List<BusinessOut> toCollectionOut(List<Business> listBusinesss) {
		return listBusinesss.stream().map(this::toOut).collect(Collectors.toList());
	}
}
