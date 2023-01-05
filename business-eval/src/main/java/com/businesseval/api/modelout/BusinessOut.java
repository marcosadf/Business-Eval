package com.businesseval.api.modelout;

import java.util.List;

import lombok.Data;

@Data
public class BusinessOut {
	private Long id;
	private String name;
	private String cnpjCpf;
	private Long managerId;
	private List<BusinessUserOut> businessUsers;
}
