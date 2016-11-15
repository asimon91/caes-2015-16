package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.*;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by deidas on 13/11/16.
 */
public class TestAccountService_delete {
    @Tested
    AccountService acs;
    Account acc = new Account("from", 0);

    // Tests Delete(Account) -------------------------------------------------------

    @Test
    public void DeleteOK(@Injectable final EntityManager em, @Injectable final CreditCardService ccs, @Mocked final CreditCard visa) throws Exception{
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(visa);
        new Expectations(){{
            visa.getCredit(); result = 0;
            em.getCreditCards(acc); result  = cards;
            ccs.delete(visa);
        }};
        acs.delete(acc);
        new Verifications(){{
            acs.delete(acc); times = 1;
            ccs.delete(visa); times = 1;
        }};
    }

    @Test(expected = AccountActive.class)
    public void DeleteAccountIsActive(@Mocked final Account mockAcc) throws Exception {
        new Expectations(){{
            mockAcc.getBalance(); result = 1000;
        }};
        acs.delete(mockAcc);
    }

    @Test(expected = AccountActive.class)
    public void DeleteAccountCreditCardIsActive(@Injectable final EntityManager em, @Mocked final Account mockAcc, @Mocked final CreditCard MasterCard) throws Exception {
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(MasterCard);
        new Expectations(){{
            mockAcc.getBalance(); result = 0;
            MasterCard.getCredit(); result = 1000;
            em.getCreditCards(mockAcc); result  = cards;
        }};
        acs.delete(mockAcc);
    }

    @Test(expected = PersistenceException.class)
    public void DeletePersistenceError(@Injectable final EntityManager em, @Injectable final CreditCardService ccs, @Mocked final CreditCard MasterCard) throws Exception{
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(MasterCard);
        new Expectations(){{
            MasterCard.getCredit(); result = 0;
            em.getCreditCards(acc); result  = cards;
            ccs.delete(MasterCard); result = new PersistenceException();
        }};
        acs.delete(acc);
    }

    @Test(expected = NotEnoughBalance.class)
    public void DeleteNotEnoughBalanceError(@Injectable final EntityManager em, @Injectable final CreditCardService ccs, @Mocked final CreditCard MasterCard) throws Exception{
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(MasterCard);
        new Expectations(){{
            MasterCard.getCredit(); result = 0;
            em.getCreditCards(acc); result  = cards;
            ccs.delete(MasterCard); result = new NotEnoughBalance();
        }};
        acs.delete(acc);
    }

    // END tests Delete(Account) ----------------------------------------------------

    // Tests Delete(Class ID) -------------------------------------------------------
    //throws AccountNotFound, AccountActive, PersistenceException, NotEnoughBalance

    @Test
    public void DeleteIdOK(@Injectable final EntityManager em, @Injectable final CreditCardService ccs, @Mocked final CreditCard MasterCard) throws Exception {
        final List<CreditCard> cards = new ArrayList<CreditCard>();
        cards.add(MasterCard);
        new Expectations(){{
            em.get("from", Account.class); result = acc;
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc);
        }};
        acs.delete(acc.getId());
        new Verifications(){{
            acs.delete(acc.getId()); times = 1;
        }};
    }

    @Test(expected = AccountNotFound.class)
    public void DeleteIdAccountNotFoundError(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("from", Account.class); result = acc;
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); result = new AccountNotFound();
        }};
        acs.delete(acc.getId());
    }

    @Test(expected = AccountActive.class)
    public void DeleteIdAccountIsActiveError(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("from", Account.class); result = acc;
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); result = new AccountActive();
        }};
        acs.delete(acc.getId());
    }

    @Test(expected = PersistenceException.class)
    public void DeleteIdPersistenceError(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("from", Account.class); result = acc;
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); result = new PersistenceException();
        }};
        acs.delete(acc.getId());
    }

    @Test(expected = NotEnoughBalance.class)
    public void DeleteIdNotEnoughBalanceError(@Injectable final EntityManager em) throws Exception{
        new Expectations(){{
            em.get("from", Account.class); result = acc;
        }};
        new Expectations(AccountService.class){{
            acs.delete(acc); result = new NotEnoughBalance();
        }};
        acs.delete(acc.getId());
    }

    // END tests Delete(Class ID) ---------------------------------------------------

}
