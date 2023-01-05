package com.businesseval.api.modelout;

import lombok.Data;

@Data
public class AnswerOut {
	private Long id;
	private Long businessUserId;
	private Long questionId;
	private Integer value;
}
