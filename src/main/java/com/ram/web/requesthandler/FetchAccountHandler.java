package com.ram.web.requesthandler;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.ram.domain.Account;
import com.ram.exception.StructuralException;
import com.ram.service.AccountService;
import com.ram.util.Constants;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class FetchAccountHandler implements Route {

    public static final String PATH = "/api/accounts/:accountId";
    private AccountService accountService;

    @Inject
    public FetchAccountHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        validate(request.params(":accountId"));
        Account account = accountService.fetchAccount(Long.valueOf(request.params(":accountId")));
        response.type(Constants.APPLICATION_JSON);
        response.status(HttpStatus.OK_200);
        return account;

    }

    private void validate(String accountId) throws StructuralException {
        try {
            Long.valueOf(accountId);
        } catch (NumberFormatException ex) {
            throw new StructuralException(ImmutableMap.of("accountId", "invalid account Id"));
        }
    }
}
