package com.mycoolcar.registration.listener;

import com.mycoolcar.entities.User;
import com.mycoolcar.registration.OnResetPasswordEvent;
import com.mycoolcar.services.IUserService;
import com.mycoolcar.util.MessageSourceHandler;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    private final IUserService userService;

    private final MessageSourceHandler messageSourceHandler;

    private final JavaMailSender mailSender;

    private final Environment env;

    @Autowired
    public ResetPasswordListener(IUserService userService, MessageSourceHandler messageSourceHandler, JavaMailSender mailSender, Environment env) {
        this.userService = userService;
        this.messageSourceHandler = messageSourceHandler;
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


    private SimpleMailMessage constructEmailMessage(final OnResetPasswordEvent event, final User user,
                                                    final String token) {

        final String recipientAddress = user.getEmail();
        final String subject = "Reset password";
        final String confirmationUrl = getAppUrl(event.getRequest()) + "/password/change?token=" + token;

        final String message = messageSourceHandler
                .getLocalMessage("message.resetPassword", event.getRequest(),
                        "You sent the request to Reset Password, if you want to continue, " +
                                "please proceed by link, if you do not want - ignore this message");
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(WebRequest request) {
        return request.getHeader("Origin") + request.getContextPath();
    }

}
