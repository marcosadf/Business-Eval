package com.businesseval.api.modelout;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserOut {
	private Long id;
	private String name;
	private String email;
	private String authority;
	@JsonIgnore
	private Date expirationCode;
	private List<BusinessUserOut> businessUsers;
	private List<BusinessOut> businesses;
	public Boolean getExpirated() {
        if (expirationCode == null) {
            return null;
        } else {
            return expirationCode.before(new Date(System.currentTimeMillis()));
        }
	}
}
