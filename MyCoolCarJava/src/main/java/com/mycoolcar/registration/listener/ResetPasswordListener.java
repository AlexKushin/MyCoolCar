package com.mycoolcar.registration.listener;

import com.mycoolcar.entities.User;
import com.mycoolcar.registration.OnResetPasswordEvent;
import com.mycoolcar.services.IUserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    private final IUserService userService;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final Environment env;

    @Autowired
    public ResetPasswordListener(IUserService userService, MessageSource messageSource, JavaMailSender mailSender, Environment env) {
        this.userService = userService;
        this.messages = messageSource;
        this.mailSender = mailSender;
        this.env = env;
    }

    @Override
    public void onApplicationEvent(@NonNull OnResetPasswordEvent event) {
        this.resetPassword(event);
    }

    private void resetPassword(final OnResetPasswordEvent event) {
        final User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);
        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }


    private SimpleMailMessage constructEmailMessage(final OnResetPasswordEvent event, final User user, final String token) {

        final String recipientAddress = user.getEmail();
        final String subject = "Reset password";
        final String confirmationUrl = event.getAppUrl() + "/password/change?token=" + token;
        final String message = messages.getMessage("message.resetPassword",
                null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
