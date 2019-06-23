package com.ram.domain;

import com.ram.exception.InsufficientAccountBalanceException;
import com.ram.util.Constants;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.math.BigDecimal;

/**
 * Created by Ravi on 21-Jun-19.
 */
@Entity
public class Account {
    @Id
    private Long accountId;
    private String name;
    private BigDecimal balance;
    private long version;

    private Account() {

    }

    public Account(Long accountId, String name, BigDecimal balance) {
        this.accountId = accountId;
        this.name = name;
        this.balance = format(balance);
    }

    private BigDecimal format(BigDecimal balance) {
        return balance.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void withdraw(BigDecimal transferAmount) throws InsufficientAccountBalanceException {
        if (balance.compareTo(format(transferAmount)) == -1) {
            throw new InsufficientAccountBalanceException(Constants.INSUFFICIENT_BALANCE);
        }

        balance = balance.subtract(transferAmount);
    }

    public void deposit(BigDecimal transferAmount) {
        balance = balance.add(format(transferAmount));
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
