package com.mycoolcar.exceptions;

import java.io.Serial;

public final class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 693091659392899768L;

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(final String message) {
        super(message);
    }

    public UserNotFoundException(final Throwable cause) {
        super(cause);
    }

}
