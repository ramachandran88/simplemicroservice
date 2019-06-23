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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static spark.Spark.exception;
import static spark.Spark.get;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class FetchAccountHandlerTest extends TestHelper {

    private static AccountService accountService;
    private static ObjectMapper mapper;

    @AfterClass
    public static void tearDown() {
        Spark.stop();
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
        get(FetchAccountHandler.PATH, Constants.APPLICATION_JSON, new FetchAccountHandler(accountService), responseTransformer);
        Spark.awaitInitialization();
    }

    @Test
    public void shouldFetchAccountForValidRequest() throws IOException {

        when(accountService.fetchAccount(10L)).thenReturn(new Account(10L, "ram", BigDecimal.valueOf(100.55)));
        UrlResponse response = getMethod("/api/accounts/10");

        Map map = mapper.readValue(response.body, Map.class);

        assertThat(response.status).isEqualTo(200);
        assertThat((String)map.get("name")).isEqualTo("ram");
    }

    @Test
    public void shouldGiveStructuralErrorForInvalidRequest() throws IOException {
        UrlResponse response = getMethod("/api/accounts/abc");

        List list = mapper.readValue(response.body, List.class);
        Map<String, String> map = (Map<String, String>) list.get(0);
        assertThat(response.status).isEqualTo(400);
        assertThat((String)map.get("key")).isEqualTo("accountId");
        assertThat((String)map.get("message")).isEqualTo("invalid account Id");
    }


}