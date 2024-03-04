package com.mycoolcar.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@AllArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ApiResponse("Malformed JSON Request", ex.getMessage()), status);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        String errorsStr = ex.getBindingResult().getAllErrors().stream().map(e -> {
            if (e instanceof FieldError) {
                try {
                    return """
                            {"field":" + %s ","message":"%s"}
                            """
                            .formatted(((FieldError) e).getField(), messageSource
                                    .getMessage(e.getCode() + ".user." + ((FieldError) e).getField(), null, locale));

                } catch (NoSuchMessageException exception) {
                    return """
                            {"field":" + %s ","defaultMessage":"%s"}
                            """
                            .formatted(((FieldError) e).getField(), e.getDefaultMessage());
                }
            } else {
                try {
                    return """
                            {"object":" + %s ","message":"%s"}
                            """
                            .formatted(e.getObjectName(), messageSource
                                    .getMessage(e.getCode() + ".user", null, locale));
                } catch (NoSuchMessageException exception) {
                    return """
                            {"object":" + %s ","defaultMessage":"%s"}
                            """
                            .formatted(e.getObjectName(), e.getDefaultMessage());
                }

            }
        }).collect(Collectors.joining(","));
        ApiResponse apiResponse = new ApiResponse(errorsStr, "Method Argument Not Valid");
        return handleExceptionInternal(ex, apiResponse, new HttpHeaders(), NOT_ACCEPTABLE, request);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        ApiResponse error = new ApiResponse("No Handler Found", ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException ex, final WebRequest request) {
        ApiResponse error = new ApiResponse("User already exists", ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), CONFLICT, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, final WebRequest request) {
        ApiResponse error = new ApiResponse("Resource not found", ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), BAD_REQUEST, request);
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
