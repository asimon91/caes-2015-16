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

    @Test
    public void DeleteOK() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        clientAccounts.add(acc);
        new Expectations(){{
            em.get("user1", Client.class); result = cli;
            em.getClientAccounts(cli); result = clientAccounts;
            acs.delete(acc);
        }};
        cs.delete("user1");
        new Verifications(){{
            acs.delete(acc); times = 1;
        }};
    }

    @Test
    public void DeleteWithoutAccountsOK() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        new Expectations(){{
            em.get("user1", Client.class); result = cli;
            em.getClientAccounts(cli); result = clientAccounts;
        }};
        cs.delete("user1");
        new Verifications(){{
            acs.delete((Account) any); times = 0;
        }};
    }

    @Test(expected = ClientNotFound.class)
    public void DeleteClientNotFoundError() throws Exception {
        new Expectations(){{
            em.get("user1", Client.class); result = new EntityNotFound();
        }};
        cs.delete("user1");
    }

    @Test(expected = PersistenceException.class)
    public void DeletePersistenceExceptionError() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        clientAccounts.add(acc);
        new Expectations(){{
            em.get("user1", Client.class); result = cli;
            em.getClientAccounts(cli); result = clientAccounts;
            acs.delete(acc); result = new PersistenceException();
        }};
        cs.delete("user1");
    }

    @Test(expected = AccountActive.class)
    public void DeleteAccountActiveError() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        clientAccounts.add(acc);
        new Expectations(){{
            em.get("user1", Client.class); result = cli;
            em.getClientAccounts(cli); result = clientAccounts;
            acs.delete(acc); result = new AccountActive();
        }};
        cs.delete("user1");
    }

    @Test(expected = NotEnoughBalance.class)
    public void DeleteNotEnoughBalanceError() throws Exception {
        final List<Account> clientAccounts = new ArrayList<Account>();
        clientAccounts.add(acc);
        new Expectations(){{
            em.get("user1", Client.class); result = cli;
            em.getClientAccounts(cli); result = clientAccounts;
            acs.delete(acc); result = new NotEnoughBalance();
        }};
        cs.delete("user1");
    }
}
