package com.businesseval.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.businesseval.api.assembler.UserAssembler;
import com.businesseval.api.modelin.UserIn;
import com.businesseval.api.modelout.UserOut;
import com.businesseval.domain.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/requirelogin")
public class loginController {
	private UserService userService;
	private UserAssembler assembler;
	
	@PostMapping
	public UserOut requireLogin(@RequestBody UserIn user) {
		return assembler.toOut(userService.requireLoginCode(user.getEmail(), user.getCustomTitle(), user.getCustomMessage()));
	}	
}
