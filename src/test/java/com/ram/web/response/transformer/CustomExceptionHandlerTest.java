package com.ram.web.response.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ram.exception.CustomException;
import com.ram.web.config.JacksonModule;
import com.ram.web.response.ErrorResponse;
import com.ram.web.response.error.ErrorData;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import spark.Response;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.times;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class CustomExceptionHandlerTest {
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new JacksonModule());
        objectMapper = injector.getInstance(ObjectMapper.class);
    }

    @Test
    public void shouldSetErrorResponse() throws Exception {
        Response mockResponse = Mockito.mock(Response.class);
        CustomExceptionHandler handler = new CustomExceptionHandler(objectMapper);
        handler.handle(new TestCustomException("invalid name"), null, mockResponse);
        Mockito.verify(mockResponse, times(1)).status(HttpStatus.BAD_REQUEST_400);
        String errorJson = "[{\"key\":\"test\",\"message\":\"invalid name\"}]";
        Mockito.verify(mockResponse, times(1)).body(errorJson);
    }

    static class TestCustomException extends CustomException {

        public TestCustomException(String message) {
            super(message);
        }

        @Override
        public ErrorResponse format() {
            return new ErrorResponse(HttpStatus.BAD_REQUEST_400, newArrayList(
                    new ErrorData("test", getMessage())));
        }
    }
}