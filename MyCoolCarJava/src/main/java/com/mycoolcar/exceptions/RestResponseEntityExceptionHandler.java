package com.mycoolcar.exceptions;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.mycoolcar.util.ApiResponse;
import com.mycoolcar.util.MessageSourceHandler;
import com.opencsv.exceptions.CsvValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSourceHandler messageSourceHandler;

    private static final String CANT_READ_FILE = "Can't read file";

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ApiResponse errorResponse = new ApiResponse(status, "Malformed JSON Request");
        return new ResponseEntity<>(errorResponse, status);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        final BindingResult result = ex.getBindingResult();
        String errorsStr = result.getAllErrors().stream().map(e -> {
            if (e instanceof FieldError fieldError) {
                return " %s : %s"
                        .formatted(fieldError.getField(), messageSourceHandler
                                .getLocalMessage(e.getCode() + ".user." + fieldError.getField(),
                                        request, fieldError.getDefaultMessage()));
            } else {
                return " %s :  %s"
                        .formatted(e.getObjectName(), messageSourceHandler
                                .getLocalMessage(e.getCode() + ".user", request, e.getDefaultMessage()));
            }
        }).collect(Collectors.joining(", "));
        ApiResponse errorResponse = new ApiResponse(status, errorsStr);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {

        String message = messageSourceHandler
                .getLocalMessage("exception.NoHandlerFoundException", request, ex.getMessage());
        ApiResponse errorResponse = new ApiResponse(HttpStatus.NOT_ACCEPTABLE, message);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }


    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<ApiResponse> handleUserAlreadyExistException(UserAlreadyExistException ex,
                                                                          final WebRequest request) {
        String message = messageSourceHandler
                .getLocalMessage("exception.UserAlreadyExistException", request, ex.getMessage());
        ApiResponse errorResponse = new ApiResponse(HttpStatus.CONFLICT, message);
        return new ResponseEntity<>(errorResponse, CONFLICT);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                          final WebRequest request) {
        String message = messageSourceHandler
                .getLocalMessage("exception.ResourceNotFoundException", request, ex.getMessage());
        ApiResponse errorResponse = new ApiResponse(HttpStatus.NOT_FOUND, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex,
                                                                      final WebRequest request) {
        String message = messageSourceHandler
                .getLocalMessage("exception.UserNotFoundException", request, ex.getMessage());
        ApiResponse errorResponse = new ApiResponse(HttpStatus.NOT_FOUND, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ApiResponse> handleUsernameNotFoundException(UsernameNotFoundException ex,
                                                                      final WebRequest request) {
        String message = messageSourceHandler
                .getLocalMessage("exception.UsernameNotFoundException", request, ex.getMessage());
        ApiResponse errorResponse = new ApiResponse(HttpStatus.NOT_FOUND, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(IOException.class)
    protected void handleIOException(IOException ex) {
        log.error("Can't read file by path {}", ex.getMessage());
        throw new InterruptAppException(CANT_READ_FILE);
    }

    @ExceptionHandler(AmazonServiceException.class)
    protected void handleAmazonServiceException(AmazonServiceException ex) {
        log.error("AmazonServiceException while uploading file: {}", ex.getMessage());
        throw new InterruptAppException(CANT_READ_FILE);
    }

    @ExceptionHandler(SdkClientException.class)
    protected void handleSdkClientException(SdkClientException ex) {
        log.error("SdkClientException while uploading file: {}", ex.getMessage());
        throw new InterruptAppException(CANT_READ_FILE);
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

    @ExceptionHandler(InterruptAppException.class)
    protected ResponseEntity<ApiResponse> handleInterruptAppException(InterruptAppException ex,
                                                                      final WebRequest request) {
        String message = messageSourceHandler
                .getLocalMessage("exception.InterruptAppException", request, ex.getMessage());
        ApiResponse errorResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex,
                                                                        final WebRequest request) {
        String message = messageSourceHandler
                .getLocalMessage("exception.AuthenticationException", request, ex.getMessage());
        ApiResponse errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

}
