package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.Client;
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

    @Injectable EntityManager em;

    @Mocked Client cli;

    @Mocked Account acc1;

    @Mocked Account acc2;

    final List<Account> clientAccounts = new ArrayList<Account>();

    @Test
    public void GetBalanceOK() throws Exception {
        clientAccounts.add(acc1);
        clientAccounts.add(acc2);
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
            acc1.getBalance(); result = 200;
            acc2.getBalance(); result = 50;
        }};
        long balance = cs.getBalance(cli);
        assertEquals(balance, 250);
    }

    @Test
    public void GetBalanceWithoutAccountsOK() throws Exception {
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
        }};
        long balance = cs.getBalance(cli);
        assertEquals(balance, 0);
    }
}
