package com.businesseval.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.businesseval.common.GenerateAlphaNumericString;
import com.businesseval.config.LocaleConfig;
import com.businesseval.domain.exception.BusinessException;
import com.businesseval.domain.exception.EntityNotFoundException;
import com.businesseval.domain.exception.ExpirationCodeException;
import com.businesseval.domain.model.Authority;
import com.businesseval.domain.model.User;
import com.businesseval.domain.repository.BusinessRepository;
import com.businesseval.domain.repository.BusinessUserRepository;
import com.businesseval.domain.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BusinessRepository businessRepository;
	
	@Autowired
	private BusinessUserRepository businessUserRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private MailService mailService;

	private MessageSource messageSource = new LocaleConfig().messageSource();
	
	@Value("#{T(Boolean).parseBoolean('${app.code.expiration}')}")
	private boolean CODE_EXPIRATION;
	@Value("${app.code.time.expiration}")
	private Integer CODE_TIME_EXPIRATION;
	
	public User save(User user) {
		if(user.getId() != null) {
			Optional<User> researchedUser = userRepository.findById(user.getId());
			if(researchedUser.isPresent()) {
				if(!researchedUser.get().getEmail().equals(user.getEmail())) {
					if(!userRepository.findByEmail(user.getEmail()).isEmpty()) {
						throw new BusinessException(messageSource.getMessage("email.user.exist", null, LocaleContextHolder.getLocale()));
					}
				}
				if(user.getAuthority() == Authority.ROOT && researchedUser.get().getAuthority() != Authority.ROOT) {
					throw new BusinessException(messageSource.getMessage("not.permission.operation", null, LocaleContextHolder.getLocale()));
				}
			}
			else {
				throw new EntityNotFoundException(messageSource.getMessage("add.user.not.found", null, LocaleContextHolder.getLocale()));
			}
		}else if(!userRepository.findByEmail(user.getEmail()).isEmpty()) {
			throw new BusinessException(messageSource.getMessage("email.user.exist", null, LocaleContextHolder.getLocale()));
		}
		if(user.getAuthority() == null) {
			user.setAuthority(Authority.DEFAULT);
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

	public User search(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
		}
		return user.get();
	}
	
	public User searchByEmail(String email) {
		Optional<User> users = userRepository.findByEmail(email);
		if(users.isEmpty()) {
			throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
		}
		return users.get();
	}

	public ResponseEntity<Void> delete(Long userId) {
		User user = search(userId);
		List<User> usersRoot = userRepository.findAll().stream().filter(u -> u.getAuthority() == Authority.ROOT).collect(Collectors.toList());
		if(usersRoot.size() == 1) {
			if(usersRoot.get(0).equals(user)) {
				throw new BusinessException(messageSource.getMessage("invalid.operation.last.user.root", null, LocaleContextHolder.getLocale()));
			}
		}
		user.getBusinesses().forEach(b -> {
			b.setManager(search(0L));
			businessRepository.save(b);
		});
		user.getBusinessUsers().forEach(b -> {
			b.setUser(search(0L));
			businessUserRepository.save(b);
		});
		userRepository.deleteById(userId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	public User requireLoginCode(String email, String customTitle, String customMessage) {
		Optional<User> userSearch = userRepository.findByEmail(email);
		String loginCode = GenerateAlphaNumericString.getRandomString(6);
		User user = new User();
		if(userSearch.isEmpty()) {
			user = new User();
			user.setEmail(email);
			user.setAuthority(Authority.DEFAULT);
		}else {
			user = userSearch.get(); 
		}
		if(CODE_EXPIRATION) {
			Date expirantion = new Date(System.currentTimeMillis() + (CODE_TIME_EXPIRATION * 1000));
			user.setExpirationCode(expirantion);
		}
		String encodeLoginCode = encoder.encode(loginCode);
		user.setLoginCode(encodeLoginCode);
		
		if(customTitle != null) {
			if(!customTitle.equals("")) {
				if(customMessage != null) {
					if(!customMessage.equals("")) {	
						mailService.sendMail(email, customTitle, customMessage + ": " + loginCode);
					}else {
						mailService.sendMail(email, customTitle, messageSource.getMessage("mail.code.content", null, LocaleContextHolder.getLocale()) + ": " + loginCode);
					}
				}else {
					mailService.sendMail(email, customTitle, messageSource.getMessage("mail.code.content", null, LocaleContextHolder.getLocale()) + ": " + loginCode);
				}
			}else {
				if(customMessage != null) {
					if(!customMessage.equals("")) {	
						mailService.sendMail(email, messageSource.getMessage("mail.code.title", null, LocaleContextHolder.getLocale()), customMessage + ": " + loginCode);
					}else {
						mailService.sendMail(email, messageSource.getMessage("mail.code.title", null, LocaleContextHolder.getLocale()), messageSource.getMessage("mail.code.content", null, LocaleContextHolder.getLocale()) + ": " + loginCode);
					}
				}else {
					mailService.sendMail(email, messageSource.getMessage("mail.code.title", null, LocaleContextHolder.getLocale()), messageSource.getMessage("mail.code.content", null, LocaleContextHolder.getLocale()) + ": " + loginCode);
				}
			}
		}else {
			if(customMessage != null) {
				if(!customMessage.equals("")) {	
					mailService.sendMail(email, messageSource.getMessage("mail.code.title", null, LocaleContextHolder.getLocale()), customMessage + ": " + loginCode);
				}else {
					mailService.sendMail(email, messageSource.getMessage("mail.code.title", null, LocaleContextHolder.getLocale()), messageSource.getMessage("mail.code.content", null, LocaleContextHolder.getLocale()) + ": " + loginCode);
				}
			}else {
				mailService.sendMail(email, messageSource.getMessage("mail.code.title", null, LocaleContextHolder.getLocale()), messageSource.getMessage("mail.code.content", null, LocaleContextHolder.getLocale()) + ": " + loginCode);
			}
		}
		return save(user);
	}
	
	public Boolean compareCode(User user) {
		User researchedUser = searchByEmail(user.getEmail());
		if(encoder.matches(user.getLoginCode(), researchedUser.getLoginCode())) {
			System.out.println(researchedUser.getExpirationCode());
			if(CODE_EXPIRATION) {
				if(researchedUser.getExpirationCode().before(new Date(System.currentTimeMillis()))) {
					throw new ExpirationCodeException(messageSource.getMessage("login.code.expiraded", null, LocaleContextHolder.getLocale()));
				}
			}
			return true;
		}
		return false;
	}

	public ResponseEntity<Void> deleteForLogin(User user) {
		User researchedUser = searchByEmail(user.getEmail());
		if(!compareCode(user)) {
			throw new BusinessException(messageSource.getMessage("login.code.incorrect", null, LocaleContextHolder.getLocale()));
		}
		delete(researchedUser.getId());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	public User editAuthority(Long userId, String authority) {
		Optional<User> researchedUser = userRepository.findById(userId);
		if(researchedUser.isPresent()) {
			User user = researchedUser.get();
			Authority authorityEnum;
			try {
				authorityEnum = Authority.valueOf(authority);
			}catch (Exception e) {
				throw new BusinessException(messageSource.getMessage("user.invalid.authority", null, LocaleContextHolder.getLocale()));
			}
			List<User> usersRoot = userRepository.findAll().stream().filter(u -> u.getAuthority() == Authority.ROOT).collect(Collectors.toList());
			if(usersRoot.size() == 1) {
				if(usersRoot.get(0).equals(user) && authorityEnum != Authority.ROOT) {
					throw new BusinessException(messageSource.getMessage("invalid.operation.last.user.root", null, LocaleContextHolder.getLocale()));
				}
			}
			user.setAuthority(authorityEnum);
			return save(user);
		}else {
			throw new EntityNotFoundException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
		}
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
