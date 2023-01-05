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

import com.businesseval.api.assembler.BusinessUserAssembler;
import com.businesseval.api.modelin.BusinessUserIn;
import com.businesseval.api.modelout.BusinessUserOut;
import com.businesseval.domain.repository.BusinessUserRepository;
import com.businesseval.domain.service.BusinessUserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/businessusers")
public class BusinessUserAdminController {
	private BusinessUserService businessUserService;
	private BusinessUserRepository businessUserRepository;
	private BusinessUserAssembler businessUserAssembler;
	
	@PostMapping
	public BusinessUserOut save(@Valid @RequestBody BusinessUserIn businessUser) {
		return businessUserAssembler.toOut(businessUserService.save(businessUserAssembler.toIn(businessUser)));
	}
	
	@GetMapping("/{businessUserId}")
	public BusinessUserOut search(@PathVariable Long businessUserId) {
		return businessUserAssembler.toOut(businessUserService.search(businessUserId));
	}

	@GetMapping
	public List<BusinessUserOut> listAll(){
		return businessUserAssembler.toCollectionOut(businessUserRepository.findAll());
	}
	
	@DeleteMapping("/{businessUserId}")
	public ResponseEntity<Void> delete(@PathVariable Long businessUserId){
		return businessUserService.delete(businessUserId);
	}
}