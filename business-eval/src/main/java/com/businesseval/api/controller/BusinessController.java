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

import com.businesseval.api.assembler.BusinessAssembler;
import com.businesseval.api.modelin.BusinessIn;
import com.businesseval.api.modelin.TextRequest;
import com.businesseval.api.modelout.BusinessOut;
import com.businesseval.common.ExtractUserJWT;
import com.businesseval.domain.service.BusinessService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/businesses")
public class BusinessController {
	private BusinessService businessService;
	private BusinessAssembler businessAssembler;
	
	@PostMapping
	public BusinessOut saveSelf(@Valid @RequestBody BusinessIn business , @RequestHeader HttpHeaders headers) {
		return businessAssembler.toOut(businessService.saveSetManager(businessAssembler.toIn(business), ExtractUserJWT.extract(headers)));
	}
	
	@PutMapping("/{businessId}")
	public BusinessOut editCreated(@PathVariable Long businessId ,@Valid @RequestBody BusinessIn business, @RequestHeader HttpHeaders headers) {
		return businessAssembler.toOut(businessService.editCreated(businessId, businessAssembler.toIn(business), ExtractUserJWT.extract(headers)));
	}
	
	@GetMapping("/{businessId}")
	public BusinessOut searchCreated(@PathVariable Long businessId, @RequestHeader HttpHeaders headers) {
		return businessAssembler.toOut(businessService.searchCreated(businessId, ExtractUserJWT.extract(headers)));
	}

	@GetMapping("/cnpjcpf")
	public BusinessOut searchCreatedCnpjCpf(@RequestBody TextRequest businessEmail, @RequestHeader HttpHeaders headers){
		return businessAssembler.toOut(businessService.searchCreatedByCnpjCpf(businessEmail.getText(), ExtractUserJWT.extract(headers)));
	}

	@GetMapping
	public List<BusinessOut> listSelfAll(@RequestHeader HttpHeaders headers){
		return businessAssembler.toCollectionOut(businessService.listCreated(ExtractUserJWT.extract(headers)));
	}
	
	@DeleteMapping("/{businessId}")
	public ResponseEntity<Void> deleteCreated(@PathVariable Long businessId, @RequestHeader HttpHeaders headers){
		return businessService.deleteCreated(businessId, ExtractUserJWT.extract(headers));
	}
}
