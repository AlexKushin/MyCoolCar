package com.mycoolcar.registration;


import com.mycoolcar.entities.User;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.request.WebRequest;


@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final WebRequest request;
    private final User user;

    public OnRegistrationCompleteEvent(final User user, final WebRequest request, final String appUrl) {
        super(user);
        this.user = user;
        this.request = request;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public WebRequest getRequest() {
        return request;
    }

    public User getUser() {
        return user;
    }

}
