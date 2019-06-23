package com.ram.domain;

import com.ram.exception.InsufficientAccountBalanceException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class AccountTest {

    @Test
    public void shouldWithdrawalIfBalanceIsGreaterThanWithdrawAmount() throws Exception {
        Account account = new Account(10L,"Ram", BigDecimal.valueOf(10.50));
        account.withdraw(BigDecimal.valueOf(5.25));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(5.25));
    }

    @Test
    public void shouldWithdrawIfBalanceIsEqualToWithdrawAmount() throws Exception {
        Account account = new Account(10L,"Ram", BigDecimal.valueOf(1000.50));
        account.withdraw(BigDecimal.valueOf(1000.50));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(0.00).setScale(2));
    }

    @Test
    public void shouldNotWithdrawIfBalanceIsLessThanWithdrawAmount() throws Exception {
        Account account = new Account(10L,"Ram", BigDecimal.valueOf(1000.50));
        assertThatThrownBy(()-> account.withdraw(BigDecimal.valueOf(2000.5)))
        .isInstanceOf(InsufficientAccountBalanceException.class);
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(1000.50).setScale(2));
    }

    @Test
    public void shouldDepositAmount() throws Exception {
        Account account = new Account(10L,"Ram", BigDecimal.valueOf(1000.50));
        account.deposit(BigDecimal.valueOf(1000.5));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(2001.00).setScale(2));
    }
}