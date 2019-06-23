package com.ram.web.requesthandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.ram.exception.StructuralException;
import com.ram.service.AccountService;
import com.ram.util.Constants;
import com.ram.web.request.MoneyTransferRequest;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.validation.Validator;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class MoneyTransferHandler implements Route {

    public static final String PATH = "/api/money-transfer";
    private ObjectMapper objectMapper;
    private Validator validator;
    private AccountService accountService;

    @Inject
    public MoneyTransferHandler(AccountService accountService, Validator validator, ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        MoneyTransferRequest moneyTransferRequest = buildRequest(request);
        validateRequest(moneyTransferRequest);
        accountService.transferMoney(moneyTransferRequest);
        response.type(Constants.APPLICATION_JSON);
        response.status(HttpStatus.OK_200);
        return Constants.SUCCESS;
    }

    private void validateRequest(MoneyTransferRequest moneyTransferRequest) throws StructuralException {
        Map<String, String> errors = validator.validate(moneyTransferRequest)
                .stream()
                .collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(), violation -> violation.getMessage()));

        if (errors.size() > 0) {
            throw new StructuralException(errors);
        }
    }

    private MoneyTransferRequest buildRequest(Request request) {
        try {
            return objectMapper.readValue(request.body(), MoneyTransferRequest.class);
        } catch (IOException ex) {
            return null;
        }
    }
}
