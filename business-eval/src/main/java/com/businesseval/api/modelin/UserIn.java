package com.businesseval.api.modelin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserIn {
	private Long id = null;
	@Size(max = 80)
	private String name;
	
	@NotBlank
	@Size(max = 60)
	private String email;

	private String customTitle;
	
	private String customMessage;
}
