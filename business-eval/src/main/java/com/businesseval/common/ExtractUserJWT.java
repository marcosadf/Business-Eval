package com.businesseval.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import com.businesseval.config.ApplicationContextLoad;
import com.businesseval.domain.model.User;
import com.businesseval.domain.service.UserService;

import io.jsonwebtoken.Jwts;

public class ExtractUserJWT {
	@Value("${app.token.attribute}")
	private static final String TOKEN_ATTRIBUTE = "Authorization";
	@Value("${app.token.prefix}")
	private static final String TOKEN_PREFIX = "Bearer";
	@Value("${app.token.secret}")
	private static final String TOKEN_SECRET = "525da2b8-7ccb-485c-b591-01e70ad55574";
	
	public static User extract(String tokenRequest) {
		if(tokenRequest != null) {
			String token = tokenRequest.replace(TOKEN_PREFIX+" ", "").trim();
			
//			Extrai username do token
			String username = Jwts.parser()
					.setSigningKey(TOKEN_SECRET)
					.parseClaimsJws(token)
					.getBody().getSubject();
			if(username != null) {
				User user = ApplicationContextLoad
						.getApplicationContext()
						.getBean(UserService.class)
						.searchByEmail(username);
				return user;
			}
		}
		return null;
	}
	
	public static User extract(HttpHeaders headers) {
		String tokenRequest = headers.getFirst(TOKEN_ATTRIBUTE);
		if(tokenRequest != null) {
			String token = tokenRequest.replace(TOKEN_PREFIX+" ", "").trim();
			
//			Extrai username do token
			String username = Jwts.parser()
					.setSigningKey(TOKEN_SECRET)
					.parseClaimsJws(token)
					.getBody().getSubject();
			if(username != null) {
				User user = ApplicationContextLoad
						.getApplicationContext()
						.getBean(UserService.class)
						.searchByEmail(username);
				return user;
			}
		}
		return null;
	}
}
