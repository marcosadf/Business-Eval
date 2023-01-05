package com.businesseval.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ObjectAssembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public Object toOut(Object object) {
		return modelMapper.map(object, Object.class);
	}

	public List<Object> toCollectionOut(List<Object> listObject) {
		return listObject.stream().map(this::toOut).collect(Collectors.toList());
	}
}
