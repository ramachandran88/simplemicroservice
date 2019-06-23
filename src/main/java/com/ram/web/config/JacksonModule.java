package com.ram.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class JacksonModule extends AbstractModule {

    @Provides
    @Singleton
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    protected void configure() {

    }
}
