package com.ram.web.requesthandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.ram.domain.Account;
import com.ram.exception.StructuralException;
import com.ram.service.AccountService;
import com.ram.util.Constants;
import com.ram.web.request.OpenAccountRequest;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.validation.Validator;
import java.io.IOException;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class OpenAccountHandler implements Route {

    public static final String PATH = "/api/open-account";
    private ObjectMapper objectMapper;
    private Validator validator;
    private AccountService accountService;

    @Inject
    public OpenAccountHandler(AccountService accountService, Validator validator, ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        OpenAccountRequest openAccountRequest = buildRequest(request);
        validateRequest(openAccountRequest);
        Account account = accountService.openAccount(openAccountRequest);
        response.type(Constants.APPLICATION_JSON);
        response.status(HttpStatus.CREATED_201);
        return account;
    }

    private void validateRequest(OpenAccountRequest openAccountRequest) throws StructuralException {
        Map<String, String> errors = validator.validate(openAccountRequest)
                .stream()
                .collect(toMap(violation -> violation.getPropertyPath().toString(), violation -> violation.getMessage()));

        if (errors.size() > 0) {
            throw new StructuralException(errors);
        }
    }

    private OpenAccountRequest buildRequest(Request request) {
        try {
            return objectMapper.readValue(request.body(), OpenAccountRequest.class);
        } catch (IOException ex) {
            return null;
        }
    }
}
