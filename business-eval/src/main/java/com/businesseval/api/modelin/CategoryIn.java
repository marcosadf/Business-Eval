package com.businesseval.api.modelin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CategoryIn {
	private Long id = null;
	@Size(max = 45)
	@NotBlank
	private String name;
	private Long position;
}
