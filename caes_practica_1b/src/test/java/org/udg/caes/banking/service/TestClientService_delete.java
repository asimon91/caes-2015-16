package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.Client;
import org.udg.caes.banking.exceptions.*;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deidas on 15/11/16.
 */
public class TestClientService_delete {
    @Tested
    ClientService cs;

    @Injectable EntityManager em;

    @Injectable AccountService acs;

    @Mocked Client cli;

    @Mocked Account acc;

    @Mocked Account acc2;

    @Test
    public void DeleteOK() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        clientAccounts.add(acc);
        clientAccounts.add(acc2);
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
        }};
        new Expectations(ClientService.class){{
            cs.getClient("user1"); result = cli;
        }};
        cs.delete("user1");
        new Verifications(){{
            acs.delete(acc); times = 1;
            acs.delete(acc2); times = 1;
        }};
    }

    @Test
    public void DeleteWithoutAccountsOK() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
        }};
        new Expectations(ClientService.class){{
            cs.getClient("user1"); result = cli;
        }};
        cs.delete("user1");
        new Verifications(){{
            acs.delete((Account) any); times = 0;
        }};
    }

    @Test(expected = ClientNotFound.class)
    public void DeleteClientNotFoundError() throws Exception {
        new Expectations(ClientService.class){{
            cs.getClient("user1"); result = new ClientNotFound();
        }};
        cs.delete("user1");
    }

    @Test(expected = PersistenceException.class)
    public void DeletePersistenceExceptionError() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        clientAccounts.add(acc);
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
            acs.delete(acc); result = new PersistenceException();
        }};
        new Expectations(ClientService.class){{
            cs.getClient("user1"); result = cli;
        }};
        cs.delete("user1");
    }

    @Test(expected = AccountActive.class)
    public void DeleteAccountActiveError() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        clientAccounts.add(acc);
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
            acs.delete(acc); result = new AccountActive();
        }};
        new Expectations(ClientService.class){{
            cs.getClient("user1"); result = cli;
        }};
        cs.delete("user1");
    }

    @Test(expected = NotEnoughBalance.class)
    public void DeleteNotEnoughBalanceError() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        clientAccounts.add(acc);
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
            acs.delete(acc); result = new NotEnoughBalance();
        }};
        new Expectations(ClientService.class){{
            cs.getClient("user1"); result = cli;
        }};
        cs.delete("user1");
    }
}
