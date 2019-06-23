package com.ram.web.response;

import com.ram.web.response.error.ErrorData;

import java.util.List;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class ErrorResponse {
    private int status;
    private List<ErrorData> errorMessages;

    private ErrorResponse() {
    }

    public ErrorResponse(int status, List<ErrorData> errorMessages) {
        this.status = status;
        this.errorMessages = errorMessages;
    }

    public int getStatus() {
        return status;
    }

    public List<ErrorData> getErrorMessages() {
        return errorMessages;
    }
}
