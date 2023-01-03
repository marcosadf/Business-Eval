package com.businesseval.domain.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "manager")
public class User {
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(max = 80)
	private String name;
	
	@NotBlank
	@Size(max = 60)
	private String email;
	
	@Size(max = 255)
	private String loginCode;
	
	@JsonIgnore
	private Date expirationCode;
	
	@Column(columnDefinition = "string default 'DEFAULT'")
	@Enumerated(EnumType.STRING)
	private Authority authority;
	
	@JsonIgnore
	@OneToMany(mappedBy = "manager", cascade = CascadeType.REFRESH)
	private List<Business> businesses; 
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<BusinessUser> BusinessUsers;
	
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		if(authority == Authority.DEFAULT) {
			return List.of(new SimpleGrantedAuthority("DEFAULT"));
		}
		else if(authority == Authority.MANAGER) {
			return List.of(new SimpleGrantedAuthority("DEFAULT"), new SimpleGrantedAuthority("MANAGER"));
		}
		else if(authority == Authority.ROOT) {
			return List.of(new SimpleGrantedAuthority("DEFAULT"), new SimpleGrantedAuthority("MANAGER"), new SimpleGrantedAuthority("ROOT"));
		}
		return null;
	}
}
