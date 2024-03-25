package com.mycoolcar.exceptions;

import com.opencsv.exceptions.CsvValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
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
            if (e instanceof FieldError fieldError) {
                try {
                    return """
                            {"field":" + %s ","message":"%s"}
                            """
                            .formatted(fieldError.getField(), messageSource
                                    .getMessage(e.getCode() + ".user." + fieldError.getField(), null, locale));

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
        ApiResponse error = new ApiResponse(getLocalMessage("exception.NoHandlerFoundException", request), ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException ex, final WebRequest request) {
        ApiResponse error = new ApiResponse(getLocalMessage("exception.UserAlreadyExistException", request), ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), CONFLICT, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, final WebRequest request) {
        ApiResponse error = new ApiResponse(getLocalMessage("exception.ResourceNotFoundException", request), ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, final WebRequest request) {
        ApiResponse error = new ApiResponse(getLocalMessage("exception.UserNotFoundException", request), ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(IOException.class)
    protected void handleIOException(IOException ex) {
        log.error("Can't read file by path {}", ex.getMessage());
        throw new InterruptAppException("Can't read file");
    }

    @ExceptionHandler(CsvValidationException.class)
    protected void handleCsvValidationException(CsvValidationException ex) {
        log.error("Error processing CSV file: {}", ex.getMessage());
        throw new InterruptAppException("Error processing CSV file");
    }

    @ExceptionHandler(FileNotFoundException.class)
    protected void handleFileNotFoundException(FileNotFoundException ex) {
        log.error("There is no file to read {}", ex.getMessage());
        throw new InterruptAppException("There is no file to read");
    }


    private Locale getLocaleFromRequest(WebRequest request) {
        String localeParam = request.getParameter("local");
        if (localeParam != null) {
            return new Locale(localeParam);
        } else {
            return new Locale("en");
        }
    }

    private String getLocalMessage(String exCode, final WebRequest request) {
        Locale locale = getLocaleFromRequest(request);
        return messageSource.getMessage(exCode, null, locale);
    }

}
