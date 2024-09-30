package com.mycoolcar.registration.listener;


import com.mycoolcar.entities.User;
import com.mycoolcar.registration.OnRegistrationCompleteEvent;
import com.mycoolcar.services.IUserService;
import com.mycoolcar.util.MessageSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final IUserService service;

    private final MessageSourceHandler messageSourceHandler;

    private final JavaMailSender mailSender;

    private final Environment env;

    @Autowired
    public RegistrationListener(IUserService service, MessageSourceHandler messageSourceHandler,
                                JavaMailSender mailSender, Environment env) {
        this.service = service;
        this.messageSourceHandler = messageSourceHandler;
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


    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user,
                                                    final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        String appUrl = getAppUrl(event.getRequest());
        final String confirmationUrl = appUrl + "/registration/confirm?token=" + token;

        final String message1 = messageSourceHandler
                .getLocalMessage("message.regSuccInputToken", event.getRequest(),
                "You registered successfully. " +
                        "To confirm your registration please input Token on confirmation window " + "\r\n" +
                        "Token: ");
        final String message2 = messageSourceHandler
                .getLocalMessage("message.regSuccLink", event.getRequest(),
                "Or click on the below link.");
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message1 + token + " \r\n" + message2 + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(WebRequest request) {
        return request.getHeader("Origin") + request.getContextPath();
    }


}
