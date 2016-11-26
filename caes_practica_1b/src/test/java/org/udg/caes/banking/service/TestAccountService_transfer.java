package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.exceptions.AccountNotFound;
import org.udg.caes.banking.exceptions.EntityNotFound;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.Assert.*;

/**
 * Created by deidas on 13/11/16.
 */
public class TestAccountService_transfer {
    @Tested
    AccountService acs;

    @Injectable EntityManager em;

    Account from = new Account("from", 5000);
    Account to = new Account("to", 0);

    @Test
    public void TransferOK() throws Exception {
        new Expectations(AccountService.class){{
            acs.getAccount("from"); result = from;
            acs.getAccount("to"); result = to;
        }};
        acs.transfer(from.getId(), to.getId(), 5000);
        assertEquals(from.getBalance(), 0);
        assertEquals(to.getBalance(), 5000);
        new Verifications(){{
            em.persist(from); times = 1;
            em.persist(to); times = 1;
        }};
    }

    @Test(expected = NotEnoughBalance.class)
    public void TransferNotEnoughAmount() throws Exception {
        new Expectations(AccountService.class){{
            acs.getAccount("from"); result = from;
            acs.getAccount("to"); result = to;
        }};
        acs.transfer(from.getId(), to.getId(), 6000);
    }

    @Test(expected = PersistenceException.class)
    public void TransferFromNotPersistent() throws Exception {
        new Expectations(){{
            em.persist(from); result = new PersistenceException();
        }};
        new Expectations(AccountService.class){{
            acs.getAccount("from"); result = from;
            acs.getAccount("to"); result = to;
        }};
        acs.transfer(from.getId(), to.getId(), 50);
    }

    @Test(expected = PersistenceException.class)
    public void TransferToNotPersistent() throws Exception {
        new Expectations(){{
            em.persist(to); result = new PersistenceException();
        }};
        new Expectations(AccountService.class){{
            acs.getAccount("from"); result = from;
            acs.getAccount("to"); result = to;
        }};
        acs.transfer(from.getId(), to.getId(), 50);
    }

    @Test(expected = AccountNotFound.class)
    public void TransferFromInexistentAccount() throws Exception {
        new Expectations(AccountService.class){{
            acs.getAccount("foo"); result = new AccountNotFound();
        }};
        acs.transfer("foo", to.getId(), 150);
    }

    @Test(expected = AccountNotFound.class)
    public void TransferToInexistentAccount(@Mocked final Account foo)throws Exception {
        new Expectations(AccountService.class){{
            acs.getAccount("foo"); result = foo;
            acs.getAccount("bar"); result = new AccountNotFound();
        }};
        acs.transfer("foo", "bar", 123);
    }
}
