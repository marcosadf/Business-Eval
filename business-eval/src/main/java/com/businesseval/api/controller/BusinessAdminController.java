package com.businesseval.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.assembler.BusinessAssembler;
import com.businesseval.api.modelin.BusinessIn;
import com.businesseval.api.modelin.TextRequest;
import com.businesseval.api.modelout.BusinessOut;
import com.businesseval.domain.repository.BusinessRepository;
import com.businesseval.domain.service.BusinessService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/admin/businesses")
public class BusinessAdminController {
	private BusinessService businessService;
	private BusinessRepository businessRepository;
	private BusinessAssembler businessAssembler;
	
	@PostMapping
	public BusinessOut save(@Valid @RequestBody BusinessIn business) {
		return businessAssembler.toOut(businessService.save(businessAssembler.toIn(business)));
	}
	
	@PutMapping("/{businessId}")
	public BusinessOut edit(@PathVariable Long businessId ,@Valid @RequestBody BusinessIn business) {
		return businessAssembler.toOut(businessService.edit(businessId, businessAssembler.toIn(business)));
	}
	
	@GetMapping("/{businessId}")
	public BusinessOut search(@PathVariable Long businessId) {
		return businessAssembler.toOut(businessService.search(businessId));
	}

	@GetMapping("/name/contains")
	public List<BusinessOut> searchNameContains(@RequestBody TextRequest businessName){
		return businessAssembler.toCollectionOut(businessRepository.findByNameContains(businessName.getText()));
	}

	@GetMapping("/cnpjcpf")
	public BusinessOut searchCnpjCpf(@RequestBody TextRequest businessEmail){
		return businessAssembler.toOut(businessService.searchByCnpjCpf(businessEmail.getText()));
	}

	@GetMapping
	public List<BusinessOut> listAll(){
		return businessAssembler.toCollectionOut(businessRepository.findAll());
	}
	
	@DeleteMapping("/{businessId}")
	public ResponseEntity<Void> delete(@PathVariable Long businessId){
		return businessService.delete(businessId);
	}
}
