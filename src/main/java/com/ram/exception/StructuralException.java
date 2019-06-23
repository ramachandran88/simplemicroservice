package com.ram.exception;

import com.ram.web.response.ErrorResponse;
import com.ram.web.response.error.ErrorData;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class StructuralException extends CustomException {

    private Map<String, String> violations;

    public StructuralException(Map<String, String> violations) {
        super();
        this.violations = violations;
    }

    @Override
    public ErrorResponse format() {
        List<ErrorData> errors = this.violations.entrySet()
                .stream()
                .map(entry -> new ErrorData(entry.getKey(), entry.getValue()))
                .collect(toList());
        return new ErrorResponse(HttpStatus.BAD_REQUEST_400, errors);
    }
}
