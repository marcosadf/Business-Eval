package com.businesseval.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.businesseval.domain.model.User;
import com.businesseval.security.UserDetailsDate;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = Optional.of(userService.searchByEmail(username));
		return new UserDetailsDate(user);
	}

}
