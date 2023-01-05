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

import com.businesseval.api.assembler.UserAssembler;
import com.businesseval.api.modelin.TextRequest;
import com.businesseval.api.modelin.UserIn;
import com.businesseval.api.modelout.UserOut;
import com.businesseval.domain.repository.UserRepository;
import com.businesseval.domain.service.UserService;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserAdminController {
	private UserService userService;
	private UserRepository userRepository;
	private UserAssembler userAssembler;
	
	@PostMapping
	public UserOut save(@Valid @RequestBody UserIn user) {
		return userAssembler.toOut(userService.save(userAssembler.toIn(user)));
	}
	
	@PutMapping("/{userId}")
	public UserOut edit(@PathVariable Long userId ,@Valid @RequestBody UserIn user) {
		return userAssembler.toOut(userService.edit(userId, userAssembler.toIn(user)));
	}
	
	@GetMapping("/{userId}")
	public UserOut search(@PathVariable Long userId) {
		return userAssembler.toOut(userService.search(userId));
	}

	@GetMapping("/name/contains")
	public List<UserOut> searchNameContains(@RequestBody TextRequest userName){
		return userAssembler.toCollectionOut(userRepository.findByNameContains(userName.getText()));
	}

	@GetMapping("/email")
	public UserOut searchByEmail(@RequestBody TextRequest userEmail){
		return userAssembler.toOut(userService.searchByEmail(userEmail.getText()));
	}

	@GetMapping("/email/contains")
	public List<UserOut> searchEmailContains(@RequestBody TextRequest userEmail){
		return userAssembler.toCollectionOut(userRepository.findByEmailContains(userEmail.getText()));
	}

	@GetMapping
	public List<UserOut> listAll(){
		return userAssembler.toCollectionOut(userRepository.findAll());
	}
	
	@PutMapping("/setauthority/{userId}")
	public UserOut editAuthority(@PathVariable Long userId ,@Valid @RequestBody TextRequest authority) {
		return userAssembler.toOut(userService.editAuthority(userId, authority.getText()));
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> delete(@PathVariable Long userId){
		return userService.delete(userId);
	}
}
