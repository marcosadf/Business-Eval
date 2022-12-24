package com.businesseval.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.domain.model.User;
import com.businesseval.domain.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/requirelogin")
public class loginController {
	private UserService userService;
	
	@PostMapping
	public User requireLogin(@RequestBody User user) {
		return userService.requireLoginCode(user.getEmail());
	}
	
}
