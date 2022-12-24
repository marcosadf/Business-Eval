package com.businesseval.domain.service;



import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.businesseval.config.LocaleConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {
	@Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private static String sender;
    private MessageSource messageSource = new LocaleConfig().messageSource();

    public void sendMail(String to, String title, String content) {
        log.info(messageSource.getMessage("mail.simple.sending", null, LocaleContextHolder.getLocale()));

        var message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        javaMailSender.send(message);
        log.info(messageSource.getMessage("mail.success.sent", null, LocaleContextHolder.getLocale()));
    }

    public void sendMailWithAttachment(String to, String title, String content, String file) throws MessagingException {
        log.info(messageSource.getMessage("mail.attachment.sending", null, LocaleContextHolder.getLocale()));
        var message = javaMailSender.createMimeMessage();

        var helper = new MimeMessageHelper(message, true);
        
        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(title);
        helper.setText(content, true);
        
        String[] nameFile = file.split("\\");
        
        helper.addAttachment(nameFile.length > 0 ? nameFile[nameFile.length-1]: "", new ClassPathResource(file));

        javaMailSender.send(message);
        log.info(messageSource.getMessage("mail.attachment.success.sent", null, LocaleContextHolder.getLocale()));
    }
}
