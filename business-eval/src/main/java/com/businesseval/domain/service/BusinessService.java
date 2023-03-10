package com.businesseval.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.exception.BusinessException;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.model.Business;
import com.businesseval.domain.model.User;
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
				if(!researchedBusiness.get().getCnpjCpf().equals(business.getCnpjCpf())) {
					if(!businessRepository.findByCnpjCpf(business.getCnpjCpf()).isEmpty()) {
						throw new BusinessException(messageSource.getMessage("cnpjcpf.business.exist", null, LocaleContextHolder.getLocale()));
					}
				}
			}
			else {
				throw new EntityNotFoundException(messageSource.getMessage("add.business.not.found", null, LocaleContextHolder.getLocale()));
			}
		}else if(!businessRepository.findByCnpjCpf(business.getCnpjCpf()).isEmpty()) {
			throw new BusinessException(messageSource.getMessage("cnpjcpf.business.exist", null, LocaleContextHolder.getLocale()));
		}
		return businessRepository.save(business);
	}
	
	public Business edit(Long businessId, Business business) {
		search(businessId);
		business.setId(businessId);
		return save(business);	
	}
	
	public ResponseEntity<Void> delete(Long businessId) {
		search(businessId);
		businessRepository.deleteById(businessId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	
	// ----------------------------------------User Root----------------------------------------
	public Business search(Long businessId) {
		Optional<Business> business = businessRepository.findById(businessId);
		if(business.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return business.get();
	}
	
	public Business searchByCnpjCpf(String businessCnpjCpf) {
		List<Business> business = businessRepository.findByCnpjCpf(businessCnpjCpf);
		if(business.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return business.get(0);
	}
	
	// ----------------------------------------User Default----------------------------------------
	public Business saveSetManager(Business business, User user) {
		business.setManager(user);
		return save(business);
	}
	
	public List<Business> listCreated(User user) {
		if(user.getBusinesses().isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return user.getBusinesses();
	}
	
	public Business searchCreated(Long businessId, User user) {
		if(user.getBusinesses().isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		List<Business> businesses = user.getBusinesses().stream().filter(b -> b.getId() == businessId).collect(Collectors.toList());
		if(businesses.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return businesses.get(0);
	}
	
	public Business searchCreatedByCnpjCpf(String businessCnpjCpf, User user) {
		if(user.getBusinesses().isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		List<Business> businesses = user.getBusinesses().stream().filter(b -> b.getCnpjCpf().equals(businessCnpjCpf)).collect(Collectors.toList());
		if(businesses.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return businesses.get(0);
	}
	
	public List<Business> listCreatedByNameContains(String name, User user) {
		if(user.getBusinesses().isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		List<Business> businesses = user.getBusinesses().stream().filter(b -> b.getName().contains(name)).collect(Collectors.toList());
		if(businesses.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("business.not.found", null, LocaleContextHolder.getLocale()));
		}
		return businesses;
	}
	
	public Business editCreated(Long businessId, Business business, User user) {
		Business businessResearched = searchCreated(businessId, user);
		business.setManager(user);
		return edit(businessResearched.getId(), business);
	}
	
	public ResponseEntity<Void> deleteCreated(Long businessId, User user) {
		Business businessResearched = searchCreated(businessId, user);
		return delete(businessResearched.getId());
	}
	
}
