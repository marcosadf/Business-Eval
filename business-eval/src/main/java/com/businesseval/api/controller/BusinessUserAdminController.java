package com.businesseval.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.domain.model.BusinessUser;
import com.businesseval.domain.repository.BusinessUserRepository;
import com.businesseval.domain.service.BusinessUserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/businessUsers")
public class BusinessUserAdminController {
	private BusinessUserService businessUserService;
	private BusinessUserRepository businessUserRepository;
	
	@PostMapping
	public BusinessUser save(@Valid @RequestBody BusinessUser businessUser) {
		return businessUserService.save(businessUser);
	}
	
	@GetMapping("/{businessUserId}")
	public BusinessUser search(@PathVariable Long businessUserId) {
		return businessUserService.search(businessUserId);
	}

	@GetMapping
	public List<BusinessUser> listAll(){
		return businessUserRepository.findAll();
	}
	
	@DeleteMapping("/{businessUserId}")
	public ResponseEntity<Void> delete(@PathVariable Long businessUserId){
		return businessUserService.delete(businessUserId);
	}
}