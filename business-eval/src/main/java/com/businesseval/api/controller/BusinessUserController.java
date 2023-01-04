package com.businesseval.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.common.ExtractUserJWT;
import com.businesseval.domain.model.BusinessUser;
import com.businesseval.domain.service.BusinessUserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/businessUsers")
public class BusinessUserController {
	private BusinessUserService businessUserService;
	
	@PostMapping
	public BusinessUser saveSelf(@Valid @PathVariable BusinessUser businessUser, @RequestHeader HttpHeaders headers) {
		return businessUserService.saveSelf(businessUser, ExtractUserJWT.extract(headers));
	}
	
	@GetMapping("/{businessUserId}")
	public BusinessUser searchCreated(@PathVariable Long businessUserId, @RequestHeader HttpHeaders headers) {
		return businessUserService.searchCreated(businessUserId, ExtractUserJWT.extract(headers));
	}
	
	@DeleteMapping("/{businessUserId}")
	public ResponseEntity<Void> deleteCreated(@PathVariable Long businessUserId, @RequestHeader HttpHeaders headers) {
		return businessUserService.deleteCreated(businessUserId, ExtractUserJWT.extract(headers));
	}
	

}