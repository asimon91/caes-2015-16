package org.udg.caes.banking.service;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
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
    }

    @Test(expected = AccountNotFound.class)
    public void TransferInexistentAccount(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("from", Account.class); result = new EntityNotFound();
        }};
        acs.transfer(from.getId(), to.getId(), 150);
    }
}
