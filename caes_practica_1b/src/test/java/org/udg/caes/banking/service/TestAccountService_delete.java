package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.*;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by deidas on 13/11/16.
 */
public class TestAccountService_delete {
    @Tested
    AccountService acs;

    @Injectable EntityManager em;

    @Injectable CreditCardService ccs;

    @Injectable CreditCard visa;

    @Injectable CreditCard masterCard;

    @Mocked Account acc;

    // Tests Delete(Account) -------------------------------------------------------

    @Test
    public void DeleteOK() throws Exception{
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(visa);
        cards.add(masterCard);
        new Expectations(){{
            visa.getCredit(); result = 0;
            masterCard.getCredit(); result = 0;
            em.getCreditCards(acc); result  = cards;
            ccs.delete(visa); times = 1;
            ccs.delete(masterCard); times = 1;
            em.delete(acc); times = 1;
            acc.getBalance(); result = 0;
        }};
        acs.delete(acc);
    }

    @Test
    public void DeleteWithoutCreditCardsOK() throws Exception {
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        new Expectations(){{
            em.getCreditCards(acc); result = cards;
        }};
        acs.delete(acc);
        new Verifications(){{
            em.delete(acc); times = 1;
            ccs.delete((CreditCard) any); times = 0;
        }};
    }

    @Test(expected = AccountActive.class)
    public void DeleteAccountIsActive() throws Exception {
        new Expectations(){{
            acc.getBalance(); result = 1000;
        }};
        acs.delete(acc);
    }

    @Test(expected = AccountActive.class)
    public void DeleteAccountCreditCardIsActive() throws Exception {
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(visa);
        new Expectations(){{
            acc.getBalance(); result = 0;
            visa.getCredit(); result = 1000;
            em.getCreditCards(acc); result  = cards;
        }};
        acs.delete(acc);
    }

    @Test(expected = PersistenceException.class)
    public void DeleteCreditCardPersistenceError() throws Exception {
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(visa);
        cards.add(masterCard);
        new Expectations(){{
            visa.getCredit(); result = 0;
            masterCard.getCredit(); result = 0;
            em.getCreditCards(acc); result  = cards;
            ccs.delete(masterCard); result = new PersistenceException();
        }};
        acs.delete(acc);
    }

    @Test(expected = PersistenceException.class)
    public void DeleteAccountPersistenceError() throws Exception {
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(visa);
        cards.add(masterCard);
        new Expectations(){{
            visa.getCredit(); result = 0;
            masterCard.getCredit(); result = 0;
            em.delete(acc); result = new PersistenceException();
            em.getCreditCards(acc); result = cards;
        }};
        acs.delete(acc);
        new Verifications(){{
            ccs.delete(visa); times = 1;
        }};
    }

    @Test(expected = NotEnoughBalance.class)
    public void DeleteNotEnoughBalanceError() throws Exception {
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(visa);
        cards.add(masterCard);
        new Expectations(){{
            visa.getCredit(); result = 0;
            em.getCreditCards(acc); result  = cards;
            ccs.delete(visa); result = new NotEnoughBalance();
        }};
        acs.delete(acc);
    }

    // END tests Delete(Account) ----------------------------------------------------

    // Tests Delete(Class ID) -------------------------------------------------------

    @Test
    public void DeleteIdOK() throws Exception {
        new Expectations(){{
            acc.getId(); result = "acc";
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); times = 1;
            acs.getAccount("acc"); result = acc;
        }};
        acs.delete(acc.getId());
    }

    @Test(expected = AccountNotFound.class)
    public void DeleteIdAccountNotFoundError() throws Exception {
        new Expectations(){{
            acc.getId(); result = "acc";
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); result = new AccountNotFound();
            acs.getAccount("acc"); result = acc;
        }};
        acs.delete(acc.getId());
    }

    @Test(expected = AccountActive.class)
    public void DeleteIdAccountIsActiveError() throws Exception {
        new Expectations(){{
            acc.getId(); result = "acc";
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); result = new AccountActive();
            acs.getAccount("acc"); result = acc;
        }};
        acs.delete(acc.getId());
    }

    @Test(expected = PersistenceException.class)
    public void DeleteIdPersistenceError() throws Exception {
        new Expectations(){{
            acc.getId(); result = "acc";
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); result = new PersistenceException();
            acs.getAccount("acc"); result = acc;
        }};
        acs.delete(acc.getId());
    }

    @Test(expected = NotEnoughBalance.class)
    public void DeleteIdNotEnoughBalanceError() throws Exception {
        new Expectations(){{
            acc.getId(); result = "acc";
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); result = new NotEnoughBalance();
            acs.getAccount("acc"); result = acc;
        }};
        acs.delete(acc.getId());
    }

    // END tests Delete(Class ID) ---------------------------------------------------

}
