package com.businesseval.security;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.businesseval.domain.service.UserDetailsServiceImpl;
import com.businesseval.domain.service.UserService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@EnableMethodSecurity 
@Configuration
public class JWTConfiguration extends WebSecurityConfigurerAdapter implements HttpSessionListener{
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.POST, "/requirelogin")
		.antMatchers(HttpMethod.GET, "/requirelogin");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
//		Delabilitado apenas durante desenvolvimento, quando implementado desabilitar
		http.csrf().disable().authorizeHttpRequests()
			.antMatchers("/login").permitAll()
			.antMatchers("/").hasAuthority("DEFAULT")
			.antMatchers("/admin/**").hasAuthority("ROOT")
			.anyRequest().authenticated() 
			.and()
			.addFilter(new JWTAuthenticationFilter(authenticationManager(), userService))
			.addFilter(new JWTValidateFilter(authenticationManager()))
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}	
}