package com.businesseval.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.modelin.TextRequest;
import com.businesseval.common.ExtractUserJWT;
import com.businesseval.domain.model.Business;
import com.businesseval.domain.service.BusinessService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/businesss")
public class BusinessController {
	private BusinessService businessService;
	
	@PostMapping
	public Business save(@Valid @RequestBody Business business , @RequestHeader HttpHeaders headers) {
		return businessService.saveSetManager(business, ExtractUserJWT.extract(headers));
	}
	
	@PutMapping("/{businessId}")
	public Business edit(@PathVariable Long businessId ,@Valid @RequestBody Business business, @RequestHeader HttpHeaders headers) {
		return businessService.editCreated(businessId, business, ExtractUserJWT.extract(headers));
	}
	
	@GetMapping("/{businessId}")
	public Business search(@PathVariable Long businessId, @RequestHeader HttpHeaders headers) {
		return businessService.searchCreated(businessId, ExtractUserJWT.extract(headers));
	}

	@GetMapping("/name/contains")
	public List<Business> searchNameContains(@RequestBody TextRequest businessName, @RequestHeader HttpHeaders headers){
		return businessService.searchByNameContains(businessName.getText(), ExtractUserJWT.extract(headers));
	}

	@GetMapping("/cnpjcpf")
	public Business searchCnpjCpf(@RequestBody TextRequest businessEmail, @RequestHeader HttpHeaders headers){
		return businessService.searchCreatedByCnpjCpf(businessEmail.getText(), ExtractUserJWT.extract(headers));
	}

	@GetMapping
	public List<Business> listAll(@RequestHeader HttpHeaders headers){
		return businessService.listCreated(ExtractUserJWT.extract(headers));
	}
	
	@DeleteMapping("/{businessId}")
	public ResponseEntity<Void> delete(@PathVariable Long businessId, @RequestHeader HttpHeaders headers){
		return businessService.deleteCreated(businessId, ExtractUserJWT.extract(headers));
	}
}
