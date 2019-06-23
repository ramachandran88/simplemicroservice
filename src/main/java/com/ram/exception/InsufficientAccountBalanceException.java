package com.ram.exception;

import com.ram.web.response.ErrorResponse;
import com.ram.web.response.error.ErrorData;
import org.eclipse.jetty.http.HttpStatus;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class InsufficientAccountBalanceException extends CustomException {

    public InsufficientAccountBalanceException(String message) {
        super(message);
    }

    @Override
    public ErrorResponse format() {
        return new ErrorResponse(HttpStatus.BAD_REQUEST_400, newArrayList(
                new ErrorData("balance",getMessage())
        ));
    }
}
