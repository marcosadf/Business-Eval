package com.businesseval.api.modelin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class QuestionIn {
	private Long id = null;
	
	@Size(max = 500)
	@NotBlank
	private String description;
	
	private Long position;
	
	private Boolean positive;
	
	@NotNull
	private Long categoryId;
}
