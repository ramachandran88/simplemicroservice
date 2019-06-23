package com.ram.web.requesthandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ram.TestHelper;
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

import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static spark.Spark.exception;
import static spark.Spark.post;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class MoneyTransferHandlerTest extends TestHelper {

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
        post(MoneyTransferHandler.PATH, Constants.APPLICATION_JSON, new MoneyTransferHandler(accountService, validator, mapper), responseTransformer);
        Spark.awaitInitialization();
    }

    @Test
    public void shouldTransferMoneyForValidRequest() throws IOException {
        String moneyTransferRequest = "{\"sourceAccountId\": 10 ,\"destinationAccountId\" : 20,\"transferAmount\":50.00}";
        UrlResponse response = postMethod("/api/money-transfer", moneyTransferRequest);

        assertThat(response.status).isEqualTo(200);
    }

    @Test
    public void shouldGiveStructuralErrorForInvalidRequest() throws IOException {
        String moneyTransferRequest = "{\"sourceAccountId\": null ,\"destinationAccountId\" : 20,\"transferAmount\":50.00}";
        UrlResponse response = postMethod("/api/money-transfer", moneyTransferRequest);

        List list = mapper.readValue(response.body, List.class);
        Map<String, String> map = (Map<String, String>) list.get(0);
        assertThat(response.status).isEqualTo(400);
        assertThat((String) map.get("key")).isEqualTo("sourceAccountId");
        assertThat((String) map.get("message")).isEqualTo("must not be null");
    }


}