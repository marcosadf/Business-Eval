package com.businesseval.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.exception.BusinessException;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.model.Business;
import com.businesseval.domain.repository.BusinessRepository;

@Service
public class BusinessService {
	@Autowired
	private BusinessRepository businessRepository;
	private MessageSource messageSource = new LocaleConfig().messageSource();

	
	public Business save(Business business) {
		if(business.getId() != null) {
			Optional<Business> researchedBusiness = businessRepository.findById(business.getId());
			if(researchedBusiness.isPresent()) {
				if(researchedBusiness.get().getCnpjCpf() != business.getCnpjCpf()) {
					if(!businessRepository.findByCnpjCpf(business.getCnpjCpf()).isEmpty()) {
						throw new BusinessException(messageSource.getMessage("email.user.exist", null, LocaleContextHolder.getLocale()));
					}
				}
			}
			else {
				throw new EntityNotFoundException(messageSource.getMessage("add.user.not.found", null, LocaleContextHolder.getLocale()));
			}
		}else if(!businessRepository.findByCnpjCpf(business.getCnpjCpf()).isEmpty()) {
			throw new BusinessException(messageSource.getMessage("email.user.exist", null, LocaleContextHolder.getLocale()));
		}
		return businessRepository.save(business);
	}
	
	public Business edit(Long businessId, Business business) {
		if(businessRepository.findById(businessId).isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		business.setId(businessId);
		return save(business);	
	}
}
