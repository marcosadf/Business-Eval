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

import com.businesseval.api.modelin.TextRequest;
import com.businesseval.domain.model.Business;
import com.businesseval.domain.repository.BusinessRepository;
import com.businesseval.domain.service.BusinessService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/admin/businesss")
public class BusinessAdminController {
	private BusinessService businessService;
	private BusinessRepository businessRepository;
	
	@PostMapping
	public Business save(@Valid @RequestBody Business business) {
		return businessService.save(business);
	}
	
	@PutMapping("/{businessId}")
	public Business edit(@PathVariable Long businessId ,@Valid @RequestBody Business business) {
		return businessService.edit(businessId, business);
	}
	
	@GetMapping("/{businessId}")
	public Business search(@PathVariable Long businessId) {
		return businessService.search(businessId);
	}

	@GetMapping("/name/contains")
	public List<Business> searchNameContains(@RequestBody TextRequest businessName){
		return businessRepository.findByNameContains(businessName.getText());
	}

	@GetMapping("/cnpjcpf")
	public List<Business> searchCnpjCpf(@RequestBody TextRequest businessEmail){
		return businessRepository.findByCnpjCpf(businessEmail.getText());
	}

	@GetMapping
	public List<Business> listAll(){
		return businessRepository.findAll();
	}
	
	@DeleteMapping("/{businessId}")
	public ResponseEntity<Void> delete(@PathVariable Long businessId){
		return businessService.delete(businessId);
	}
}
