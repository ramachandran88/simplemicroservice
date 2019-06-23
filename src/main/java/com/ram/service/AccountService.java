package com.ram.service;

import com.google.inject.Inject;
import com.ram.db.repository.AccountRepository;
import com.ram.domain.Account;
import com.ram.exception.SameSourceDestinationAccountException;
import com.ram.util.Constants;
import com.ram.util.RandomGenerator;
import com.ram.web.request.MoneyTransferRequest;
import com.ram.web.request.OpenAccountRequest;

import java.util.List;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class AccountService {

    private RandomGenerator randomGenerator;
    private AccountRepository accountRepository;

    @Inject
    public AccountService(AccountRepository accountRepository, RandomGenerator randomGenerator) {
        this.accountRepository = accountRepository;
        this.randomGenerator = randomGenerator;
    }

    public Account openAccount(OpenAccountRequest openAccountRequest) {
        Long accountId = randomGenerator.randomLong();
        return accountRepository.openAccount(new Account(accountId, openAccountRequest.getName(), openAccountRequest.getBalance()));
    }

    public List<Account> fetchAllAccounts() {
        return accountRepository.findAll();
    }

    public Account fetchAccount(Long accountId) {
        return accountRepository.findByAccountId(accountId);
    }

    public void transferMoney(MoneyTransferRequest moneyTransferRequest) throws Exception {
        checkIfSameAccounts(moneyTransferRequest);
        accountRepository.transferMoney(
                moneyTransferRequest.getSourceAccountId(),
                moneyTransferRequest.getDestinationAccountId(),
                moneyTransferRequest.getTransferAmount()
        );
    }

    private void checkIfSameAccounts(MoneyTransferRequest moneyTransferRequest) throws SameSourceDestinationAccountException {
        if (moneyTransferRequest.getSourceAccountId().equals(moneyTransferRequest.getDestinationAccountId())) {
            throw new SameSourceDestinationAccountException(Constants.SAME_SOURCE_DESTINATION_ACCOUNT);
        }
    }

}
