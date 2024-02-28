package com.mycoolcar.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@RestControllerAdvice
@AllArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ApiError(status.value(), "Malformed JSON Request", ex.getMessage()), status);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        List<String> errors = new ArrayList<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String errorMessage;
            try {
                if (error.getCode() != null && !error.getCode().equals("PasswordMatches")) {
                    FieldError fieldError = (FieldError) error;
                    errorMessage = messageSource
                            .getMessage(error.getCode() + ".user." + fieldError.getField(), null, locale);
                } else {
                    errorMessage = messageSource
                            .getMessage("PasswordMatches.user", null, locale);
                }
            } catch (NoSuchMessageException e) {
                errorMessage = error.getDefaultMessage();
            }
            errors.add(errorMessage);
        }
        ApiError apiError = new ApiError(status.value(), "Method Argument Not Valid", new Date(), ex.getMessage(), errors);
        return new ResponseEntity<>(apiError, NOT_ACCEPTABLE);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ApiError(status.value(), "No Handler Found", ex.getMessage()), status);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        return new ResponseEntity<>(new ApiError(BAD_REQUEST.value(), "User already exists", ex.getMessage()), BAD_REQUEST);
    }


    private Locale getLocaleFromRequest(WebRequest request) {
        String localeParam = request.getParameter("local");
        if (localeParam != null) {
            return new Locale(localeParam);
        } else {
            return new Locale("en");
        }
    }

}
