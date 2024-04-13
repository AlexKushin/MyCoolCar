package com.mycoolcar.registration.listener;


import com.mycoolcar.entities.User;
import com.mycoolcar.registration.OnRegistrationCompleteEvent;
import com.mycoolcar.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final IUserService service;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final Environment env;

    @Autowired
    public RegistrationListener(IUserService service, MessageSource messageSource, JavaMailSender mailSender, Environment env) {
        this.service = service;
        this.messages = messageSource;
        this.mailSender = mailSender;
        this.env = env;
    }
    // API

    @Override
    public void onApplicationEvent(@NonNull final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }


    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        System.out.println("event.getLocale()= " + event.getLocale());
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registration/confirm?token=" + token;
        final String message1 = messages.getMessage("message.regSuccInputToken", null,
                "You registered successfully. " +
                        "To confirm your registration please input Token on confirmation window " + "\r\n" +
                        "Token: ", event.getLocale());
        final String message2 = messages.getMessage("message.regSuccLink", null,
                "Or click on the below link.", event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message1 + token + " \r\n" + message2 + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }


}
