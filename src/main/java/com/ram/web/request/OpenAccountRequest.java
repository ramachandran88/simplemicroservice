package com.ram.web.request;

import com.google.common.base.Objects;
import com.ram.util.Constants;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class OpenAccountRequest {
    @NotEmpty(message = Constants.INVALID_NAME)
    private String name;

    @NotNull
    @DecimalMin(value = Constants.MIN_ALLOWED_BALANCE)
    private BigDecimal balance;

    private OpenAccountRequest() {
    }

    public OpenAccountRequest(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenAccountRequest)) return false;
        OpenAccountRequest that = (OpenAccountRequest) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, balance);
    }
}
