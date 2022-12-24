package com.businesseval.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.businesseval.common.ExtractUserJWT;
import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JWTValidateFilter extends BasicAuthenticationFilter{
	@Value("${app.token.attribute}")
	private static String TOKEN_ATTRIBUTE;
	@Value("${app.token.prefix}")
	private static String TOKEN_PREFIX;
	@Value("${app.token.secret}")
	private static String TOKEN_SECRET;
	private static MessageSource messageSource = new LocaleConfig().messageSource();
	
	public JWTValidateFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String attribute = request.getHeader(TOKEN_ATTRIBUTE);
		
		if(attribute == null) {
			chain.doFilter(request, response);
			return;
		}
		
		if(!attribute.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = attribute.replace(TOKEN_PREFIX+" ", "");
		if(validateToken(token, response, request)) {
			UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			chain.doFilter(request, response);
		}
	}
	
	public boolean validateToken(String token,HttpServletResponse response, HttpServletRequest request){
	    	 Jws<Claims> jwt = Jwts.parser()
					.setSigningKey(TOKEN_SECRET)
					.parseClaimsJws(token);
					
	    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	    	if( jwt.getBody().getExpiration().before(new Date())) {
	    		response.setStatus(403);
	    		response.setHeader("Content-Type", "application/json");
	    		try {
					response.getOutputStream().print(ow.writeValueAsString(new ExpiredTokenResponse()));
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		return false;
	    	}
	    	return true;
	}
	public static class ExpiredTokenResponse{
		public int status = 403;
		public String dateTime = LocalDateTime.now().toString();         
		public String title = messageSource.getMessage("expired.token", null, LocaleContextHolder.getLocale());
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
		User user = ExtractUserJWT.extract(token);
		if(user != null) {
			return new UsernamePasswordAuthenticationToken(
					user.getEmail(),
					user.getLoginCode(), 
					user.getAuthorities());			
		}
		return null;
	}
	

}
