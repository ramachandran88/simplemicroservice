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
 * Created by Ravi on 22-Jun-19.
 */
public class MoneyTransferRequestTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        this.validator = Guice.createInjector(new ValidatorModule())
                .getInstance(Validator.class);
    }

    @Test
    public void shouldHaveNoViolationForValidRequest() throws Exception {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(10L, 400L, BigDecimal.valueOf(10.00));
        Set<ConstraintViolation<MoneyTransferRequest>> violations = validator.validate(moneyTransferRequest);
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldHaveViolationForNullSourceAccount() throws Exception {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(null, 400L, BigDecimal.valueOf(10.00));
        Set<ConstraintViolation<MoneyTransferRequest>> violations = validator.validate(moneyTransferRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("must not be null");
    }

    @Test
    public void shouldHaveViolationForNullDestinationAccount() throws Exception {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(20L, null, BigDecimal.valueOf(10.00));
        Set<ConstraintViolation<MoneyTransferRequest>> violations = validator.validate(moneyTransferRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("must not be null");
    }

    @Test
    public void shouldHaveViolationForZeroTransferAmount() throws Exception {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(20L, 400L, BigDecimal.ZERO);
        Set<ConstraintViolation<MoneyTransferRequest>> violations = validator.validate(moneyTransferRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("must be greater than or equal to 0.01");
    }

    @Test
    public void shouldHaveViolationForNegativeTransferAmount() throws Exception {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(20L, 400L, BigDecimal.valueOf(-10));
        Set<ConstraintViolation<MoneyTransferRequest>> violations = validator.validate(moneyTransferRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("must be greater than or equal to 0.01");
    }


}