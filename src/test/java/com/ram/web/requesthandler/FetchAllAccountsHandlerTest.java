package com.ram.web.requesthandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ram.TestHelper;
import com.ram.domain.Account;
import com.ram.exception.CustomException;
import com.ram.service.AccountService;
import com.ram.util.Constants;
import com.ram.web.config.JacksonModule;
import com.ram.web.config.ValidatorModule;
import com.ram.web.response.transformer.ApiResponseTransformer;
import com.ram.web.response.transformer.CustomExceptionHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import spark.Spark;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static spark.Spark.awaitStop;
import static spark.Spark.exception;
import static spark.Spark.get;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class FetchAllAccountsHandlerTest extends TestHelper {

    private static AccountService accountService;
    private static ObjectMapper mapper;

    @AfterClass
    public static void tearDown() {
        Spark.stop();
        awaitStop();
    }

    @BeforeClass
    public static void setup() {
        Injector injector = Guice.createInjector(
                new ValidatorModule(),
                new JacksonModule()
        );
        mapper = injector.getInstance(ObjectMapper.class);
        accountService = Mockito.mock(AccountService.class);
        ApiResponseTransformer responseTransformer = injector.getInstance(ApiResponseTransformer.class);
        exception(CustomException.class, injector.getInstance(CustomExceptionHandler.class));
        get(FetchAllAccountsHandler.PATH, Constants.APPLICATION_JSON, new FetchAllAccountsHandler(accountService), responseTransformer);
        Spark.awaitInitialization();
    }

    @Test
    public void shouldFetchAllAccountForValidRequest() throws IOException {
        when(accountService.fetchAllAccounts()).thenReturn(
                newArrayList(
                new Account(10L, "ram", BigDecimal.valueOf(100.55)),
                new Account(20L, "raj", BigDecimal.valueOf(100.55))
        ));
        UrlResponse response = getMethod("/api/accounts");
        List list = mapper.readValue(response.body, List.class);
        Map<String, String> map = (Map<String, String>) list.get(0);

        assertThat(response.status).isEqualTo(200);
        assertThat(list).hasSize(2);
        assertThat((String) map.get("name")).isEqualTo("ram");
    }


}