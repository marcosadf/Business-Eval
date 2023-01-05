package com.businesseval.api.modelin;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AnswerIn {
	private Long id = null;
	@NotNull
	private Long businessUserId;
	@NotNull
	private Long questionId;
	private Integer value;
}
