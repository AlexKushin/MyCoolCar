package com.mycoolcar.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1628667709705917826L;

    private String message;

    private String error;


    public ApiResponse(final String message) {
        super();
        this.message = message;

    }

    public ApiResponse(String allErrorsMessage, String error) {
        super();
        this.error = error;
        this.message = "[" + allErrorsMessage + "]";
    }

}
