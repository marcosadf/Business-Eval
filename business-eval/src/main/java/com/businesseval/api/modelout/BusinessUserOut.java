package com.businesseval.api.modelout;

import java.util.List;

import lombok.Data;

@Data
public class BusinessUserOut {
	private Long id;
	private Long userId;
	private String userEmail;
	private Long businessId;
	private Boolean invitationAccepted;
	private List<AnswerOut> answers;
}