package com.businesseval.domain.service;

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
import com.businesseval.domain.model.BusinessUser;
import com.businesseval.domain.model.User;
import com.businesseval.domain.repository.BusinessUserRepository;

@Service
public class BusinessUserService {
	@Autowired
	private BusinessUserRepository businessUserRepository;
	@Autowired
	private UserService userService;
	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	public BusinessUser save(BusinessUser businessUser) {
		if(businessUser.getId() != null || userService.search(businessUser.getUser().getId())
				.getBusinessUsers().stream()
				.anyMatch(b -> b.getBusiness().getId() == businessUser.getBusiness().getId())
				) {
				throw new BusinessException(messageSource.getMessage("businessUser.exist", null, LocaleContextHolder.getLocale()));
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
	public BusinessUser saveSelf(BusinessUser businessUser, User user) {
		if(user.getId() != businessUser.getUser().getId()) {
			throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
		}
		return save(businessUser);
	}
	
	
	public BusinessUser searchCreated(Long businessUserId, User user) {
		if(user.getBusinessUsers().isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("businessUser.not.found", null, LocaleContextHolder.getLocale()));
		}
		Optional<BusinessUser> businessUser = user.getBusinessUsers().stream()
				.filter(b -> b.getId() == businessUserId).findFirst();

		if(businessUser.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return businessUser.get();
	}
	
	public ResponseEntity<Void>  deleteCreated(Long businessUserId, User user) {
		searchCreated(businessUserId, user);
		businessUserRepository.deleteById(businessUserId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
}