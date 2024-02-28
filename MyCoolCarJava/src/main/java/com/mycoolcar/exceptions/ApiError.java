package com.mycoolcar.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private Date timeStamp;
    private String debugMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;




    public ApiError(int status, String message, String debugMessage) {
        this.status = status;
        this.message = message;
        this.timeStamp = new Date();
        this.debugMessage = debugMessage;

    }
}
