package com.businesseval.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//import com.auth0.jwt.exceptions.TokenExpiredException;
import com.businesseval.domain.exception.BusinessException;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.exception.ExpirationCodeException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	private MessageSource messageSource;
	
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		List<Problem.Field> fields = new ArrayList<>();
		
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			fields.add(new Problem.Field(nome, mensagem));
		}
		
		Problem problem = new Problem();
		problem.setStatus(status.value());
		problem.setDateTime(LocalDateTime.now());
		problem.setTitle(messageSource.getMessage("invalid.fields", null, LocaleContextHolder.getLocale()));
		problem.setFields(fields);
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleBusiness(EntityNotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		 
		Problem problem = new Problem();
		problem.setStatus(status.value());
		problem.setDateTime(LocalDateTime.now());
		problem.setTitle(ex.getMessage());
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusiness(BusinessException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		 
		Problem problem = new Problem();
		problem.setStatus(status.value());
		problem.setDateTime(LocalDateTime.now());
		problem.setTitle(ex.getMessage());
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ExpirationCodeException.class)
	public ResponseEntity<Object> handleBusiness(ExpirationCodeException ex, WebRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		 
		Problem problem = new Problem();
		problem.setStatus(status.value());
		problem.setDateTime(LocalDateTime.now());
		problem.setTitle(ex.getMessage());
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
//	@ExceptionHandler(InternalAuthenticationServiceException.class)
//	public ResponseEntity<Object> handleBusiness(TokenExpiredException ex, WebRequest request) {
//		HttpStatus status = HttpStatus.BAD_REQUEST;
//		 
//		Problem problem = new Problem();
//		problem.setStatus(status.value());
//		problem.setDateTime(LocalDateTime.now());
//		problem.setTitle(ex.getMessage());
//		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
//	}
	
}
