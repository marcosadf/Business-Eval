package com.businesseval.api.assembler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.businesseval.api.modelin.UserIn;
import com.businesseval.api.modelout.UserOut;
import com.businesseval.domain.model.Business;
import com.businesseval.domain.model.BusinessUser;
import com.businesseval.domain.model.User;

@Component
public class UserAssembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public UserAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.modelMapper.createTypeMap(User.class, UserOut.class)
		.<List<Business>>addMapping(src -> src.getBusinesses(), (src, value) -> {
			if(value != null) {
				src.setBusinesses(new BusinessAssembler(modelMapper).toCollectionOut(value));
			}else {
				src.setBusinesses(new ArrayList<>());
			}
		})
		.<List<BusinessUser>>addMapping(src -> src.getBusinessUsers(), (src, value) -> {
			if(value != null) {
				src.setBusinessUsers(new BusinessUserAssembler(modelMapper).toCollectionOut(value));
			}else {
				src.setBusinessUsers(new ArrayList<>());
			}
		});
	}
	
	public User toIn(UserIn user) {
		return modelMapper.map(user, User.class);
	}
	
	public UserOut toOut(User user) {
		return modelMapper.map(user, UserOut.class);
	}

	public List<UserOut> toCollectionOut(List<User> listUser) {
		return listUser.stream().map(this::toOut).collect(Collectors.toList());
	}
	
	public Boolean calculateExpirated(Date expirationCode) {
        if (expirationCode == null) {
            return null;
        } else {
            return expirationCode.before(new Date(System.currentTimeMillis()));
        }
	}
}
