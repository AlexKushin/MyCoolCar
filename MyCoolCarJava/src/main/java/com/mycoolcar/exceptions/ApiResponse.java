package com.mycoolcar.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {
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
