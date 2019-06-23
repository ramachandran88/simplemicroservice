package com.ram;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ram.db.DbModule;
import com.ram.exception.CustomException;
import com.ram.util.Constants;
import com.ram.web.config.JacksonModule;
import com.ram.web.config.ValidatorModule;
import com.ram.web.requesthandler.FetchAccountHandler;
import com.ram.web.requesthandler.FetchAllAccountsHandler;
import com.ram.web.requesthandler.MoneyTransferHandler;
import com.ram.web.requesthandler.OpenAccountHandler;
import com.ram.web.response.transformer.ApiResponseTransformer;
import com.ram.web.response.transformer.CustomExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Ravi on 20-Jun-19.
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("in main method");
        start();
    }

    public static void start() {
        Injector injector = injectModules();
        ApiResponseTransformer responseTransformer = injector.getInstance(ApiResponseTransformer.class);
        get("/", (req, res) -> "Application Ready for Use");
        post(OpenAccountHandler.PATH, Constants.APPLICATION_JSON, injector.getInstance(OpenAccountHandler.class),responseTransformer);
        get(FetchAllAccountsHandler.PATH, Constants.APPLICATION_JSON, injector.getInstance(FetchAllAccountsHandler.class), responseTransformer);
        get(FetchAccountHandler.PATH, Constants.APPLICATION_JSON, injector.getInstance(FetchAccountHandler.class), responseTransformer);
        post(MoneyTransferHandler.PATH, Constants.APPLICATION_JSON, injector.getInstance(MoneyTransferHandler.class), responseTransformer);

        exception(CustomException.class, injector.getInstance(CustomExceptionHandler.class));
    }


    private static Injector injectModules() {
        return Guice.createInjector(
                new DbModule(),
                new ValidatorModule(),
                new JacksonModule()
        );
    }
}
