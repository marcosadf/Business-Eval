package com.businesseval.domain.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.businesseval.domain.ValidationGroups;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class BusinessUser {
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroups.BusinessId.class)
	@ManyToOne
	@NotNull
	private Business business;
	
	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroups.UserId.class)
	@ManyToOne
	@NotNull
	private User user;
	
	@OneToMany(mappedBy = "businessUser", cascade = CascadeType.REFRESH)
	private List<Answer> answers;
}
