package com.businesseval.domain.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.ConvertGroup;

import com.businesseval.domain.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Business {
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(max = 130)
	private String name;
	
	@NotBlank
	@Size(max = 18)
	private String cnpjCpf;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "manager_id")
	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroups.ManagerId.class)
	private User manager;
	
	@JsonIgnore
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
	private List<BusinessUser> BusinessUsers; 
	
}
