package com.ram.db.repository;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ram.db.DbModule;
import com.ram.domain.Account;
import com.ram.exception.AccountNotFoundException;
import com.ram.exception.InsufficientAccountBalanceException;
import com.ram.util.Constants;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class AccountRepositoryTest {

    private static AccountRepository accountRepository;

    @Before
    public void setUp() throws Exception {
        accountRepository.deleteAll();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Injector injector = Guice.createInjector(new DbModule());
        accountRepository = injector.getInstance(AccountRepository.class);
    }

    @Test
    public void shouldSaveAccount() throws Exception {
        long accountId = 10L;
        accountRepository.openAccount(new Account(accountId, "ram", BigDecimal.TEN));
        Account actualAccount = accountRepository.findByAccountId(accountId);
        assertThat(actualAccount.getBalance()).isEqualTo(BigDecimal.TEN.setScale(2));
    }

    @Test
    public void shouldReturnNullIfAccountDoesNotExist(){
        Long nonExistingAccountId = 150L;
        Account actualAccount = accountRepository.findByAccountId(nonExistingAccountId);
        assertThat(actualAccount).isNull();
    }

    @Test
    public void shouldFetchAllAccounts() throws Exception {
        accountRepository.openAccount(new Account(10L, "ram", BigDecimal.TEN));
        accountRepository.openAccount(new Account(20L, "rahul", BigDecimal.TEN));
        accountRepository.openAccount(new Account(30L, "raj", BigDecimal.TEN));
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(3);
    }

    @Test
    public void shouldThrowErrorIfSourceAccountDoesNotExist() throws Exception {
        assertThatThrownBy(()-> accountRepository.transferMoney(10L, 20L, BigDecimal.TEN))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage(Constants.SOURCE_OR_DESTINATION_NOT_EXIST);
    }

    @Test
    public void shouldThrowErrorIfDestinationAccountDoesNotExist() throws Exception {
        assertThatThrownBy(()-> accountRepository.transferMoney(20L, 10L, BigDecimal.TEN))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage(Constants.SOURCE_OR_DESTINATION_NOT_EXIST);
    }

    @Test
    public void shouldThrowErrorIfSourceHasInsufficientBalance() throws Exception {
        accountRepository.openAccount(new Account(10L, "Ram", BigDecimal.valueOf(100.00)));
        accountRepository.openAccount(new Account(20L, "Raj", BigDecimal.valueOf(100.00)));
        assertThatThrownBy(()-> accountRepository.transferMoney(10L, 20L, BigDecimal.valueOf(200.00)))
                .isInstanceOf(InsufficientAccountBalanceException.class)
                .hasMessage(Constants.INSUFFICIENT_BALANCE);
    }


}