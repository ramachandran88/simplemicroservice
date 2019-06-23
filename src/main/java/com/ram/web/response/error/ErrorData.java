package com.ram.web.response.error;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class ErrorData {
    private String key;
    private String message;

    private ErrorData() {
    }

    public ErrorData(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }
}
