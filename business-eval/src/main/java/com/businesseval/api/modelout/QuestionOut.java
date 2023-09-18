package com.businesseval.api.modelout;

import lombok.Data;

@Data
public class QuestionOut {
	private Long id;
	private String description;
	private Long position;
	private Boolean positive;
	private Long categoryId;
}
