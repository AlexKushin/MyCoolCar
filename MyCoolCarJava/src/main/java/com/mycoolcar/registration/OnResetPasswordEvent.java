package com.mycoolcar.registration;

import com.mycoolcar.entities.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@SuppressWarnings("serial")
public class OnResetPasswordEvent extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final User user;
    public OnResetPasswordEvent(final User user, final Locale locale, final String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }
}
