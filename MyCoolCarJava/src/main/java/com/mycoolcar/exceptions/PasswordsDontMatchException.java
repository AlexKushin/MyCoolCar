package com.mycoolcar.exceptions;

import java.io.Serial;

public final class PasswordsDontMatchException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3494148845909241553L;

    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
