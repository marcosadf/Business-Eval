package com.businesseval.api.modelin;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TextRequest {
	@NotBlank
	private String text;
}
