package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.exceptions.AccountNotFound;
import org.udg.caes.banking.exceptions.EntityNotFound;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.Assert.*;

/**
 * Created by deidas on 28/10/16.
 */
public class TestAccountService_getAccount {
    @Tested
    AccountService acs;

    @Injectable EntityManager em;

    @Mocked Account ac;

    @Test
    public void GetAccountOK() throws Exception{
        new Expectations(){{
            em.get("foo", Account.class); result = ac;
        }};
        Account res = acs.getAccount("foo");
        assertSame(res, ac);
        new Verifications(){{
            em.get("foo", Account.class); times = 1;
        }};
    }

    @Test(expected = AccountNotFound.class)
    public void GetAccountFail() throws Exception{
        new Expectations(){{
            em.get("foo", Account.class); result = new EntityNotFound();
        }};
        acs.getAccount("foo");
    }
}
