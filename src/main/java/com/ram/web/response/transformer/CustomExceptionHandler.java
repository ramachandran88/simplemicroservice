package com.ram.web.response.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.ram.exception.CustomException;
import com.ram.util.Constants;
import com.ram.web.response.ErrorResponse;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class CustomExceptionHandler implements ExceptionHandler<CustomException> {

    private ObjectMapper objectMapper;

    @Inject
    public CustomExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(CustomException exception, Request request, Response response) {
        try {
            ErrorResponse errorResponse = exception.format();
            response.status(errorResponse.getStatus());
            response.type(Constants.APPLICATION_JSON);
            response.body(objectMapper.writeValueAsString(errorResponse.getErrorMessages()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
