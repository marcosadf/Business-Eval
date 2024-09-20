package com.businesseval.domain.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
public class Timeline {
//	public Timeline(String description, Date datetime_edition) {
//		this.description = description;
//		this.datetime_edition = datetime_edition != null? datetime_edition: new Date(System.currentTimeMillis());
//	}

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 255)
	private String description;
	
	private Date datetime_edition = new Date(System.currentTimeMillis());
}
