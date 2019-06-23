package com.ram.db.repository;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.ram.domain.Account;
import com.ram.exception.AccountNotFoundException;
import com.ram.exception.InsufficientAccountBalanceException;
import com.ram.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class AccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);

    public EntityManager entityManager;

    @Inject
    public AccountRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Account findByAccountId(Long accountId) {
        try {
            Account account = (Account) entityManager
                    .createQuery("select e from Account e where e.accountId=:accountId")
                    .setParameter("accountId", accountId)
                    .getSingleResult();
            entityManager.clear();
            return account;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<Account> findAll(){
        try {
            List<Account> resultList = (List<Account>) entityManager
                    .createQuery("from Account")
                    .getResultList();
            entityManager.clear();
            return resultList;
        } catch (NoResultException ex) {
            return Lists.newArrayList();
        }
    }

    public void deleteAll(){
        try {
            entityManager.getTransaction().begin();
            entityManager
                    .createQuery("DELETE from Account")
                    .executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
    }

    public Account openAccount(Account account) {
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        entityManager.clear();
        return account;
    }

    public void transferMoney(Long sourceAccountId, Long destinationAccountId, BigDecimal transferAmount)
            throws AccountNotFoundException, InsufficientAccountBalanceException {
        try {
            entityManager.getTransaction().begin();

            Account sourceAccount = findByAccountId(sourceAccountId);
            Account destinationAccount = findByAccountId(destinationAccountId);
            checkAccountsExist(sourceAccount, destinationAccount);
            transferMoney(sourceAccount, destinationAccount, transferAmount);

            entityManager.getTransaction().commit();
            entityManager.clear();
        }
        catch (Exception e){
            entityManager.getTransaction().rollback();
            throw e;
        }
        finally {
            entityManager.clear();
        }

    }

    private void transferMoney(Account sourceAccount, Account destinationAccount, BigDecimal transferAmount)
            throws InsufficientAccountBalanceException {
        sourceAccount.withdraw(transferAmount);
        destinationAccount.deposit(transferAmount);
        entityManager.merge(sourceAccount);
        entityManager.merge(destinationAccount);
    }

    private void checkAccountsExist(Account sourceAccount, Account destinationAccount) throws AccountNotFoundException {
        if (sourceAccount == null || destinationAccount == null) {
            throw new AccountNotFoundException(Constants.SOURCE_OR_DESTINATION_NOT_EXIST);
        }
    }
}
