package com.ram.web.response.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import spark.ResponseTransformer;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class ApiResponseTransformer implements ResponseTransformer {

    private ObjectMapper objectMapper;

    @Inject
    public ApiResponseTransformer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public String render(Object model) throws Exception {
        return objectMapper.writeValueAsString(model);
    }
}
