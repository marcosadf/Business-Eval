package com.businesseval.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailsDate implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private final Optional<User> user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new HashSet<>();
	}

	@Override
	public String getPassword() {
		return user.orElse(new User()).getLoginCode();
	}

	@Override
	public String getUsername() {
		return user.orElse(new User()).getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
