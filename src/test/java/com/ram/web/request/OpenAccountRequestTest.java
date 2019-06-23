package com.ram.web.request;

import com.google.inject.Guice;
import com.ram.web.config.ValidatorModule;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class OpenAccountRequestTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        this.validator = Guice.createInjector(new ValidatorModule())
                .getInstance(Validator.class);
    }

    @Test
    public void shouldHaveNoViolationForValidRequest() throws Exception {
        OpenAccountRequest openAccountRequest = new OpenAccountRequest("ram", BigDecimal.valueOf(10.00));
        Set<ConstraintViolation<OpenAccountRequest>> violations = validator.validate(openAccountRequest);
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldHaveViolationForNullName() throws Exception {
        OpenAccountRequest openAccountRequest = new OpenAccountRequest(null, BigDecimal.valueOf(10.00));
        Set<ConstraintViolation<OpenAccountRequest>> violations = validator.validate(openAccountRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("Invalid Name");
    }

    @Test
    public void shouldHaveViolationForEmptyName() throws Exception {
        OpenAccountRequest openAccountRequest = new OpenAccountRequest("", BigDecimal.valueOf(10.00));
        Set<ConstraintViolation<OpenAccountRequest>> violations = validator.validate(openAccountRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("Invalid Name");
    }

    @Test
    public void shouldHaveViolationForNullBalance() throws Exception {
        OpenAccountRequest openAccountRequest = new OpenAccountRequest("ram", null);
        Set<ConstraintViolation<OpenAccountRequest>> violations = validator.validate(openAccountRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("must not be null");
    }

    @Test
    public void shouldHaveViolationForNegativeBalance() throws Exception {
        OpenAccountRequest openAccountRequest = new OpenAccountRequest("ram", BigDecimal.valueOf(-10));
        Set<ConstraintViolation<OpenAccountRequest>> violations = validator.validate(openAccountRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("must be greater than or equal to 0.01");
    }

    @Test
    public void shouldHaveViolationForZeroBalance() throws Exception {
        OpenAccountRequest openAccountRequest = new OpenAccountRequest("ram", BigDecimal.valueOf(0));
        Set<ConstraintViolation<OpenAccountRequest>> violations = validator.validate(openAccountRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("must be greater than or equal to 0.01");
    }
}