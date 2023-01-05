package com.businesseval.api.modelin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class BusinessIn {
	private Long id = null;
	
	@Size(max = 130)
	private String name;
	
	@Size(max = 18)
	@NotBlank
	private String cnpjCpf;
}
