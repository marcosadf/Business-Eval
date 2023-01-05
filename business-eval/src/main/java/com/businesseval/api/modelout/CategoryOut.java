package com.businesseval.api.modelout;

import java.util.List;

import lombok.Data;

@Data
public class CategoryOut {
	private Long id;
	private String name;
	private Long position;
	private List<QuestionOut> questions;
}
