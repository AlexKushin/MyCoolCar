package com.mycoolcar.exceptions;

import java.io.Serial;

public final class UserAlreadyExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7910484826695897574L;

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
