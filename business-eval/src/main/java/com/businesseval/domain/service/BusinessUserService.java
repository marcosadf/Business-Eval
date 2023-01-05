package com.businesseval.domain.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.businesseval.api.modelin.BusinessUserIn;
import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.exception.BusinessException;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.model.Business;
import com.businesseval.domain.model.BusinessUser;
import com.businesseval.domain.model.User;
import com.businesseval.domain.repository.BusinessUserRepository;

@Service
public class BusinessUserService {
	@Autowired
	private BusinessUserRepository businessUserRepository;
	@Autowired
	private BusinessService businessService;
	@Autowired
	private UserService userService;
	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	public BusinessUser save(BusinessUser businessUser) {
		if(businessUser.getId() != null) {
			Business buiness = businessService.search(businessUser.getBusiness().getId());
			if(!buiness.getBusinessUsers().stream()
					.filter(b -> b.getUser().equals(businessUser.getUser()))
					.collect(Collectors.toList()).isEmpty()){
				throw new BusinessException(messageSource.getMessage("businessUser.exist", null, LocaleContextHolder.getLocale()));
			}
		}
		return businessUserRepository.save(businessUser);
	}

	
	public ResponseEntity<Void> delete(Long businessUserId) {
		search(businessUserId);
		businessUserRepository.deleteById(businessUserId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	public BusinessUser search(Long businessUserId) {
		Optional<BusinessUser> businessUser = businessUserRepository.findById(businessUserId);
		if(businessUser.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("businessUser.not.found", null, LocaleContextHolder.getLocale()));
		}
		return businessUser.get();
	}
	
	// ----------------------------------------User Default----------------------------------------
	public BusinessUser addSelf(BusinessUser businessUser, User user) {
		businessUser.setUser(user);
		businessUser.setBusiness(businessService.searchCreated(businessUser.getBusiness().getId(), user));
		return save(businessUser);
	}
	
	public BusinessUser addUser(BusinessUserIn businessUserIn, User user) {
		BusinessUser businessUser = new BusinessUser();
		businessUser.setUser(
						userService.requireLoginCode(
							businessUserIn.getUserEmail(),
							businessUserIn.getCustomTitle(),
							businessUserIn.getCustomMessage()
						)
					);
		businessUser.setBusiness(businessService.searchCreated(businessUserIn.getBusinessId(), user));	
		return save(businessUser);
	}
	
	public BusinessUser searchCreated(Long businessUserId, User user) {
		BusinessUser businessUser = search(businessUserId);
		if(!businessUser.getBusiness().getManager().equals(user)) {
			throw new EntityNotFoundException(messageSource.getMessage("not.permission.operation", null, LocaleContextHolder.getLocale()));
		}
		return businessUser;
	}
	
	public ResponseEntity<Void>  deleteCreated(Long businessUserId, User user) {
		searchCreated(businessUserId, user);
		businessUserRepository.deleteById(businessUserId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
}