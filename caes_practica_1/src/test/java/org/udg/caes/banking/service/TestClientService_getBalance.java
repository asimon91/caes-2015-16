package org.udg.caes.banking.service;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.Client;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by deidas on 15/11/16.
 */
public class TestClientService_getBalance {
    @Tested
    ClientService cs;

    Account acc1 = new Account("small", 50);
    Account acc2 = new Account("big", 200);
    final List<Account> clientAccounts = new ArrayList<Account>();

    @Test
    public void GetBalanceOK(@Injectable final EntityManager em, @Mocked final Client cli) throws Exception{
        clientAccounts.add(acc1);
        clientAccounts.add(acc2);
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
        }};
        long balance = cs.getBalance(cli);
        assertEquals(balance, 250);
    }
}
