package com.businesseval.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.businesseval.domain.model.User;
import com.businesseval.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm; 

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private AuthenticationManager authenticationManager;
	@Value("${token.attribute}")
	private static final String TOKEN_ATTRIBUTE = "Authorization";
	@Value("${token.prefix}")
	private static final String TOKEN_PREFIX = "Bearer";
	@Value("${token.secret}")
	private static final String TOKEN_SECRET = "525da2b8-7ccb-485c-b591-01e70ad55574";
	@Value("${app.token.expiration}")
	private static final boolean TOKEN_EXPIRACTION = false;
	@Value("${app.token.time.expiration}")
	private static final long TOKEN_TIME_EXPIRACTION = 86_400_000;
	
	private UserService userService;
		
	JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService){
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		
		try {
			User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			Collection<? extends GrantedAuthority> authorities = userService.findByEmail(user.getEmail()).getAuthorities();
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				user.getEmail(),
				user.getLoginCode(),
				authorities
			));
		} catch (IOException e) {
			throw new RuntimeException("Falha ao autenticar usuario", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws IOException, ServletException {
		UserDetailsDate user = (UserDetailsDate) authResult.getPrincipal();
		String jwt = Jwts.builder()
				.setSubject(user.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME_EXPIRACTION))
				.signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
				.compact();
		
		String token = TOKEN_PREFIX + " " + jwt;
		
//		Envia o token por cabeçalho da resposta
		response.addHeader(TOKEN_ATTRIBUTE, token);
		
//		Cria cabeçalho de acesso do CORS
		openingAccessCors(response);
		
//		Envia o token pelo corpo da resposta
		response.getWriter().write("{\"Authorization\":\"" + token + "\"}");
		response.getWriter().flush();
	}


//	Cria cabeçalho de acesso do CORS
	private void openingAccessCors(HttpServletResponse response) {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}

}
