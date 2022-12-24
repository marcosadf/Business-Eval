package com.businesseval.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.businesseval.api.modelin.TextRequest;
import com.businesseval.common.GenerateAlphaNumericString;
import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.exception.BusinessException;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.exception.ExpirationCodeException;
import com.businesseval.domain.model.Authority;
import com.businesseval.domain.model.User;
import com.businesseval.domain.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private MailService mailService;

	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	@Value("${code.expiration}")
	private static String CODE_EXPIRATION;
	@Value("${code.time.expiration}")
	private static Integer CODE_TIME_EXPIRATION;
	
	
	public User save(User user) {
		if(user.getId() != null) {
			Optional<User> researchedUser = userRepository.findById(user.getId());
			if(researchedUser.isPresent()) {
				if(researchedUser.get().getEmail() != user.getEmail()) {
					if(!userRepository.findByEmail(user.getEmail()).isEmpty()) {
						throw new BusinessException(messageSource.getMessage("email.user.exist", null, LocaleContextHolder.getLocale()));
					}
					if(user.getAuthority() == Authority.ROOT && researchedUser.get().getAuthority() != Authority.ROOT) {
						throw new BusinessException(messageSource.getMessage("not.permission.operation", null, LocaleContextHolder.getLocale()));
					}
				}
			}
			else {
				throw new EntityNotFoundException(messageSource.getMessage("add.user.not.found", null, LocaleContextHolder.getLocale()));
			}
		}else if(!userRepository.findByEmail(user.getEmail()).isEmpty()) {
			throw new BusinessException(messageSource.getMessage("email.user.exist", null, LocaleContextHolder.getLocale()));
		}
		return userRepository.save(user);
	}
	
	public User edit(Long userId, User user) {
		if(userRepository.findById(userId).isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
		}
		user.setId(userId);
		return save(user);	
	}

	public User findByEmail(String email) {
		List<User> users = userRepository.findByEmailContains(email);
		if(userRepository.findByEmailContains(email).isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
		}
		return users.get(0);
	}

	public ResponseEntity<Void> delete(Long userId) {
		if(userRepository.findById(userId).isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
		}
		userRepository.deleteById(userId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	public User requireLoginCode(String email) {
		List<User> users = userRepository.findByEmail(email);
		String loginCode = GenerateAlphaNumericString.getRandomString(6);
		User user = new User();
		if(users.isEmpty()) {
			user.setEmail(email);
			if(CODE_EXPIRATION == "true") {
				Date expirantion = new Date(System.currentTimeMillis() + (CODE_TIME_EXPIRATION * 1000));
				user.setExpirationCode(expirantion);
			}
			String encodeLoginCode = encoder.encode(loginCode);
			user.setLoginCode(encodeLoginCode);
			mailService.sendMail(email, messageSource.getMessage("mail.code.title", null, LocaleContextHolder.getLocale()), messageSource.getMessage("mail.code.content", null, LocaleContextHolder.getLocale()) + ": " + loginCode);
		}else{
			user = users.get(0);
			if(CODE_EXPIRATION == "true") {
				Date expirantion = new Date(System.currentTimeMillis() + (CODE_TIME_EXPIRATION * 1000));
				user.setExpirationCode(expirantion);
			}
			String encodeLoginCode = encoder.encode(loginCode);
			user.setLoginCode(encodeLoginCode);
			mailService.sendMail(email, messageSource.getMessage("mail.code.title", null, LocaleContextHolder.getLocale()), messageSource.getMessage("mail.code.content", null, LocaleContextHolder.getLocale())+ ": " + loginCode);
		}
		return save(user);
	}
	
	public ResponseEntity<Boolean> compareCode(User user) {
		User researchedUser = findByEmail(user.getEmail());
		if(researchedUser.getLoginCode() == encoder.encode(user.getLoginCode())) {
			if(CODE_EXPIRATION == "true") {
				if(researchedUser.getExpirationCode().before(new Date(System.currentTimeMillis()))) {
					throw new ExpirationCodeException(messageSource.getMessage("login.code.expiraded", null, LocaleContextHolder.getLocale()));
				}
			}
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.ok(false);
	}

	public ResponseEntity<Void> deleteForLogin(User user) {
		User researchedUser = findByEmail(user.getEmail());
		if(researchedUser.getLoginCode() != encoder.encode(user.getLoginCode())) {
			throw new BusinessException(messageSource.getMessage("login.code.incorrect", null, LocaleContextHolder.getLocale()));
		}
		userRepository.delete(researchedUser);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	public User editAuthority(Long userId, TextRequest authority) {
		Optional<User> researchedUser = userRepository.findById(userId);
		if(researchedUser.isPresent()) {
			User user = researchedUser.get();
			user.setAuthority(Authority.valueOf(authority.getText()));
		}else {
			throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
		}
		return null;
	}

	public User editSelf(Long userId, User user, User userRequest) {
		if(userRequest.getId() == userId) {
			if(userRepository.findById(userId).isEmpty()) {
				throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
			}
			user.setId(userId);
			return save(user);
		}
		throw new BusinessException(messageSource.getMessage("not.permission.operation", null, LocaleContextHolder.getLocale()));
	}
	
}
