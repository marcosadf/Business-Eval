package com.businesseval.api.modelout;

import java.util.List;

import lombok.Data;

@Data
public class UserOut {
	private Long id;
	private String name;
	private String email;
	private String authority;
	private List<BusinessUserOut> businessUsers;
	private List<BusinessOut> businesses;
}
