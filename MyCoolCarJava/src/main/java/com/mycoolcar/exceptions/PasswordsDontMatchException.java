package com.mycoolcar.exceptions;

public class PasswordsDontMatchException extends RuntimeException{

    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
