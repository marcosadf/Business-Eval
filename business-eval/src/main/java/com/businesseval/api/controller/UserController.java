package com.businesseval.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.common.ExtractUserJWT;
import com.businesseval.domain.model.User;
import com.businesseval.domain.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
	private UserService userService;

	@PutMapping("/{userId}")
	public User edit(@PathVariable Long userId ,@Valid @RequestBody User user, @RequestHeader HttpHeaders headers) {
		return userService.editSelf(userId, user, ExtractUserJWT.extract(headers));
	}
	
	@DeleteMapping("/")
	public ResponseEntity<Void> deleteForLogin(@RequestBody @Valid User user) {
		return userService.deleteForLogin(user);
	}
	
	@GetMapping
	public User searchSelf(@RequestHeader HttpHeaders headers) {
		return ExtractUserJWT.extract(headers);
	}

}
