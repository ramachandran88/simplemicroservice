package com.ram.web.requesthandler;

import com.google.inject.Inject;
import com.ram.domain.Account;
import com.ram.service.AccountService;
import com.ram.util.Constants;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class FetchAllAccountsHandler implements Route {

    public static final String PATH = "/api/accounts";
    private AccountService accountService;

    @Inject
    public FetchAllAccountsHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        List<Account> accounts = accountService.fetchAllAccounts();
        response.type(Constants.APPLICATION_JSON);
        response.status(HttpStatus.OK_200);
        return accounts;
    }

}
