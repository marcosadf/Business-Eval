package com.businesseval.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.modelin.TextRequest;
import com.businesseval.domain.model.User;
import com.businesseval.domain.repository.UserRepository;
import com.businesseval.domain.service.UserService;

import lombok.AllArgsConstructor;

@Secured({"ROOT"})
@AllArgsConstructor
@RestController
@RequestMapping("/admin/user")
public class UserRootController {
	private UserService userService;
	private UserRepository userRepository;
	
	@PostMapping
	public User save(@Valid @RequestBody User user) {
		return userService.save(user);
	}
	
	@PutMapping("/{userId}")
	public User edit(@PathVariable Long userId ,@Valid @RequestBody User user) {
		return userService.edit(userId, user);
	}
	
	@GetMapping("/name/contains")
	public List<User> searchName(@RequestBody TextRequest userName){
		return userRepository.findByNameContains(userName.getText());
	}
	
	@GetMapping("/email")
	public User findByEmail(@RequestBody TextRequest userEmail){
		return userService.findByEmail(userEmail.getText());
	}
	
	@GetMapping("/email/contains")
	public List<User> searchEmail(@RequestBody TextRequest userEmail){
		return userRepository.findByEmailContains(userEmail.getText());
	}
	
	@GetMapping
	public List<User> listAll(){
		return userRepository.findAll();
	}
	
	@PutMapping("/setAuthority/{userId}")
	public User editAuthority(@PathVariable Long userId ,@Valid @RequestBody TextRequest authority) {
		return userService.editAuthority(userId, authority);
	}
	
	@DeleteMapping("/remove/{userId}")
	public ResponseEntity<Void> delete(@PathVariable Long userId){
		return userService.delete(userId);
	}
}
