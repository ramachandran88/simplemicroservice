package com.ram.web.response.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ram.web.config.JacksonModule;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class ApiResponseTransformerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new JacksonModule());
        objectMapper = injector.getInstance(ObjectMapper.class);
    }

    @Test
    public void shouldReturnJsonString() throws Exception {
        ApiResponseTransformer responseTransformer = new ApiResponseTransformer(objectMapper);

        String actualJson = responseTransformer.render(new TestModel("ram", 20));

        TestModel testModel = objectMapper.readValue(actualJson, TestModel.class);
        assertThat(testModel.getName()).isEqualTo("ram");
        assertThat(testModel.getAge()).isEqualTo(20);

    }



    static class TestModel{
        private String name;
        private int age;

        public TestModel() {
        }

        public TestModel(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

    }
}