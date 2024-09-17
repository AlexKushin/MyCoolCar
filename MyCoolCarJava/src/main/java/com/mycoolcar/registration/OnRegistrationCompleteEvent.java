package com.mycoolcar.registration;


import com.mycoolcar.entities.User;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.request.WebRequest;


@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final WebRequest request;
    private final User user;

    public OnRegistrationCompleteEvent(final User user, final WebRequest request) {
        super(user);
        this.user = user;
        this.request = request;

    }

    public WebRequest getRequest() {
        return request;
    }

    public User getUser() {
        return user;
    }

}
