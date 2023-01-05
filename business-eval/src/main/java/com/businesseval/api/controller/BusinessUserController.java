package com.businesseval.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.assembler.BusinessUserAssembler;
import com.businesseval.api.modelin.BusinessUserIn;
import com.businesseval.api.modelout.BusinessUserOut;
import com.businesseval.common.ExtractUserJWT;
import com.businesseval.domain.service.BusinessUserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/businessusers")
public class BusinessUserController {
	private BusinessUserService businessUserService;
	private BusinessUserAssembler businessUserAssembler;
	
	@PostMapping
	public BusinessUserOut addSelf(@RequestBody BusinessUserIn businessUser, @RequestHeader HttpHeaders headers) {
		return businessUserAssembler.toOut(businessUserService.addSelf(businessUserAssembler.toIn(businessUser), ExtractUserJWT.extract(headers)));
	}
	
	@PostMapping("/add")
	public BusinessUserOut addUser(@Valid @RequestBody BusinessUserIn businessUser, @RequestHeader HttpHeaders headers) {
		return businessUserAssembler.toOut(businessUserService.addUser(businessUser, ExtractUserJWT.extract(headers)));
	}
	@GetMapping("/{businessUserId}")
	public BusinessUserOut searchCreated(@PathVariable Long businessUserId, @RequestHeader HttpHeaders headers) {
		return businessUserAssembler.toOut(businessUserService.searchCreated(businessUserId, ExtractUserJWT.extract(headers)));
	}
	
	@GetMapping
	public List<BusinessUserOut> listAllCreated(@RequestHeader HttpHeaders headers) {
		return businessUserAssembler.toCollectionOut(ExtractUserJWT.extract(headers).getBusinessUsers());
	}
	
	@DeleteMapping("/{businessUserId}")
	public ResponseEntity<Void> deleteCreated(@PathVariable Long businessUserId, @RequestHeader HttpHeaders headers) {
		return businessUserService.deleteCreated(businessUserId, ExtractUserJWT.extract(headers));
	}
}