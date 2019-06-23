package com.ram.exception;

import com.ram.web.response.error.ErrorFormatter;

/**
 * Created by Ravi on 22-Jun-19.
 */
public abstract class CustomException extends Exception implements ErrorFormatter {
    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }
}
