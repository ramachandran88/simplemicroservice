package com.ram.web.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class ValidatorModule extends AbstractModule {

    @Provides
    @Singleton
    public Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    @Override
    protected void configure() {

    }
}
