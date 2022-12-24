package com.businesseval.security;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.businesseval.domain.service.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
public class JWTConfiguration extends WebSecurityConfigurerAdapter implements HttpSessionListener{
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
	}
	
	@Override
	private void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.POST, "/requirelogin")
		.antMatchers(HttpMethod.GET, "/requirelogin");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
//		Delabilitado apenas durante desenvolvimento, quando implementado desabilitar
		http.csrf().disable().authorizeHttpRequests()
			.antMatchers(HttpMethod.POST, "/login").permitAll()
			.anyRequest().authenticated() 
			.and()
			.addFilter(new JWTAuthenticationFilter(authenticationManager()))
			.addFilter(new JWTValidateFilter(authenticationManager()))
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

//	@Bean
//	UrlBasedCorsConfigurationSource corsConfigurationSource() {
//		final UrlBasedCorsConfigurationSource source =  new UrlBasedCorsConfigurationSource();
//		
//		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
//		config.setAllowCredentials(true);
//        config.setAllowedOrigins(Collections.singletonList("*"));
//        config.addAllowedOriginPattern("*");
//        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "responseType", "Authorization"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
//		source.registerCorsConfiguration("/**" , config);
//		
//		return source;
//		
//	}
	
}