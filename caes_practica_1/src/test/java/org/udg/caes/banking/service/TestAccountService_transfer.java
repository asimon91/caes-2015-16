package org.udg.caes.banking.service;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.Assert.*;

/**
 * Created by deidas on 13/11/16.
 */
public class TestAccountService_transfer {
    @Tested
    AccountService acs;
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
}
