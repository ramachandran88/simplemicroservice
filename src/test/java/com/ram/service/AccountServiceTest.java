package com.ram.service;

import com.ram.db.repository.AccountRepository;
import com.ram.domain.Account;
import com.ram.exception.SameSourceDestinationAccountException;
import com.ram.util.Constants;
import com.ram.util.RandomGenerator;
import com.ram.web.request.MoneyTransferRequest;
import com.ram.web.request.OpenAccountRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Ravi on 21-Jun-19.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RandomGenerator randomGenerator;

    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountService(accountRepository, randomGenerator);
    }

    @Test
    public void shouldOpenNewAccount() throws Exception {
        String name = "ram";
        BigDecimal balance = BigDecimal.TEN;
        Long accountId = Long.valueOf(10L);

        when(randomGenerator.randomLong()).thenReturn(accountId);
        when(accountRepository.openAccount(any(Account.class))).thenReturn(new Account(accountId, name, balance));

        Account actualAccount = accountService.openAccount(new OpenAccountRequest(name, balance));

        verify(accountRepository, times(1)).openAccount(any(Account.class));
        assertThat(actualAccount.getAccountId()).isEqualTo(accountId);
        assertThat(actualAccount.getName()).isEqualTo(name);
    }

    @Test
    public void shouldFetchAllAccounts() throws Exception {
        when(accountRepository.findAll()).thenReturn(Collections.<Account>emptyList());
        List<Account> actualAccounts = accountService.fetchAllAccounts();

        verify(accountRepository, times(1)).findAll();
        assertThat(actualAccounts).isEmpty();
    }

    @Test
    public void shouldFetchAccount() throws Exception {
        when(accountRepository.findByAccountId(10L)).thenReturn(new Account(10L, "Ram", BigDecimal.TEN));
        Account actualAccount = accountService.fetchAccount(10L);

        verify(accountRepository, times(1)).findByAccountId(10L);
        assertThat(actualAccount.getBalance()).isEqualTo(BigDecimal.TEN.setScale(2));
    }

    @Test
    public void shouldThrowErrorIfSameSourceAndDestinationInTransferMoneyRequest() throws Exception {
        when(accountRepository.findByAccountId(10L)).thenReturn(new Account(10L, "Ram", BigDecimal.valueOf(100.00)));
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest(10L, 10L, BigDecimal.TEN);
        assertThatThrownBy(() -> accountService.transferMoney(moneyTransferRequest))
                .isInstanceOf(SameSourceDestinationAccountException.class)
                .hasMessage(Constants.SAME_SOURCE_DESTINATION_ACCOUNT);
    }

    @Test
    public void shouldTransferMoneyForValidRequest() throws Exception {
        accountService.transferMoney(new MoneyTransferRequest(10L, 20L, BigDecimal.TEN));
        verify(accountRepository, times(1)).transferMoney(10L, 20L, BigDecimal.TEN);
    }
}