package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

/**
 * Created by deidas on 17/11/16.
 */
public class TestCreditCardService_charge {
    @Tested
    CreditCardService ccs;

    @Injectable EntityManager em;

    @Mocked CreditCard visa;

    @Mocked Account acc;

    @Test
    public void ChargeOK() throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 400;
            acc.getBalance(); result = 1000;
        }};
        ccs.charge(visa, acc);
        new Verifications(){{
            acc.debit(400); times = 1;
            visa.reset(); times = 1;
            em.persist(visa); times = 1;
            em.persist(acc); times = 1;
        }};
    }

    @Test(expected = NotEnoughBalance.class)
    public void ChargeNotEnoughBalance() throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 1500;
            acc.getBalance(); result = 1000;
        }};
        ccs.charge(visa, acc);
    }

    @Test(expected = PersistenceException.class)
    public void ChargeCreditCardPersistenceExceptionError() throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 800;
            acc.getBalance(); result = 1000;
            em.persist(visa); result = new PersistenceException();
        }};
        ccs.charge(visa, acc);
    }
    @Test(expected = PersistenceException.class)
    public void ChargeAccountPersistenceExceptionError() throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 200;
            acc.getBalance(); result = 1000;
            em.persist(acc); result = new PersistenceException();
        }};
        ccs.charge(visa, acc);
    }
}
