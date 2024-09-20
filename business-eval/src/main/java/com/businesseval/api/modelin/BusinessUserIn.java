package com.businesseval.api.modelin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BusinessUserIn {
	private Long id = null;
	@NotBlank
	private String userEmail;
	@NotNull
	private Long businessId;
	
	private String customTitle;
	
	private String customMessage;

}
