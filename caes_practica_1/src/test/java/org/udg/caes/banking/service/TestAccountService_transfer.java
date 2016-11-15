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
    // I need to initialize Account objects, so
    // I instantiate them
    Account from = new Account("from", 5000);
    Account to = new Account("to", 0);

    @Test
    public void TransferOK(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("from", Account.class); result = from;
            em.get("to", Account.class); result = to;
        }};
        acs.transfer(from.getId(), to.getId(), 5000);
        assertEquals(from.getBalance(), 0);
        assertEquals(to.getBalance(), 5000);
        new Verifications(){{
            //from.debit(5000); times = 1;
            //to.credit(5000); times = 1;
            em.persist(from); times = 1;
            em.persist(to); times = 1;
        }};
    }

    @Test(expected = NotEnoughBalance.class)
    public void TransferNotEnoughAmount(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("from", Account.class); result = from;
            em.get("to", Account.class); result = to;
        }};
        acs.transfer(from.getId(), to.getId(), 6000);
    }

    @Test(expected = PersistenceException.class)
    public void TransferNotPersistent(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("from", Account.class); result = from;
            em.get("to", Account.class); result = to;
            em.persist(from); result = new PersistenceException();
        }};
        acs.transfer(from.getId(), to.getId(), 50);
        new Verifications(){{
            from.debit(50); times = 1;
            to.credit(50); times = 1;
        }};
    }

    @Test(expected = AccountNotFound.class)
    public void TransferInexistentAccount(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("foo", Account.class); result = new EntityNotFound();
        }};
        acs.transfer("foo", to.getId(), 150);
    }
}
