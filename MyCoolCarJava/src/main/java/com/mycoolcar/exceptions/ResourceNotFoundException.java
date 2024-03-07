package com.mycoolcar.exceptions;

import java.io.Serial;

public final class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3856433101032242460L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
