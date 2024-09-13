package com.mycoolcar.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.Objects;

@Service
public class MessageSourceHandler {

    private final MessageSource messageSource;

    @Autowired
    public MessageSourceHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private Locale getLocaleFromRequest(WebRequest request) {
        String localeParam = request.getParameter("local");
        return new Locale(Objects.requireNonNullElse(localeParam, "en"));
    }

    public String getLocalMessage(String exCode, final WebRequest request, String defaultMessage) {
        Locale locale = getLocaleFromRequest(request);
        return messageSource.getMessage(exCode, null, defaultMessage,locale);
    }

}
