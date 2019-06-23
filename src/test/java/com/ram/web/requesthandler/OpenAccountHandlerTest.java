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
import com.ram.web.request.OpenAccountRequest;
import com.ram.web.response.transformer.ApiResponseTransformer;
import com.ram.web.response.transformer.CustomExceptionHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import spark.Spark;

import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static spark.Spark.exception;
import static spark.Spark.post;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class OpenAccountHandlerTest extends TestHelper {

    private static ObjectMapper mapper;
    private static AccountService accountService;
    private static Validator validator;

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
        accountService = Mockito.mock(AccountService.class);
        mapper = injector.getInstance(ObjectMapper.class);
        validator = injector.getInstance(Validator.class);
        ApiResponseTransformer responseTransformer = injector.getInstance(ApiResponseTransformer.class);
        exception(CustomException.class, injector.getInstance(CustomExceptionHandler.class));
        post(OpenAccountHandler.PATH, Constants.APPLICATION_JSON, new OpenAccountHandler(accountService, validator, mapper),responseTransformer);
        Spark.awaitInitialization();
    }

    @Test
    public void shouldOpenAccountForValidRequest() throws IOException {

        String openAccountRequest = "{\"name\":\"ram\",\"balance\":100.55}";
        when(accountService.openAccount(new OpenAccountRequest("ram", BigDecimal.valueOf(100.55))))
                .thenReturn(new Account(1234L, "ram", BigDecimal.valueOf(100.55)));
        UrlResponse response = postMethod("/api/open-account", openAccountRequest);

        Map map = mapper.readValue(response.body, Map.class);
        int sourceAccountId = (Integer) map.get("accountId");

        assertThat(sourceAccountId).isNotNull();
        assertThat(response.status).isEqualTo(201);
        assertThat((String)map.get("name")).isEqualTo("ram");
    }

    @Test
    public void shouldGiveStructuralErrorForInvalidRequest() throws IOException {
        String openAccountRequest = "{\"name\":null,\"balance\":100.55}";
        UrlResponse response = postMethod("/api/open-account", openAccountRequest);

        List list = mapper.readValue(response.body, List.class);
        Map<String, String> map = (Map<String, String>) list.get(0);
        assertThat(response.status).isEqualTo(400);
        assertThat((String)map.get("key")).isEqualTo("name");
        assertThat((String)map.get("message")).isEqualTo("Invalid Name");
    }


}