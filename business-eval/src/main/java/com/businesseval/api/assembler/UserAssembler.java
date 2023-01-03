package com.businesseval.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.businesseval.api.modelout.UserOut;
import com.businesseval.domain.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UserAssembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public UserOut toOut(User user) {
		return modelMapper.map(user, UserOut.class);
	}	

	public List<UserOut> toCollectionOut(List<User> listUser) {
		return listUser.stream().map(this::toOut).collect(Collectors.toList());
	}
}
