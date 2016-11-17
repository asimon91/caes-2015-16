package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.CreditExceeded;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.Assert.*;

/**
 * Created by deidas on 28/10/16.
 */
public class TestCreditCardService_credit {
    @Tested
    CreditCardService ccs;

    @Test
    public void CreditOK(@Injectable final EntityManager em, @Mocked final CreditCard visa) throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 1500;
            visa.getMaxCredit(); result = 2000;
        }};
        ccs.credit(visa, 500);
        new Verifications(){{
            em.persist(visa); times = 1;
            visa.credit(500); times = 1;
        }};
    }

    @Test(expected = CreditExceeded.class)
    public void CreditExceededError(@Mocked final CreditCard visa) throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 1500;
            visa.getMaxCredit(); result = 3000;
        }};
        ccs.credit(visa, 2000);
    }

    @Test(expected = PersistenceException.class)
    public void CreditPersistenceExceptionError(@Injectable final EntityManager em, @Mocked final CreditCard visa) throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 100;
            visa.getMaxCredit(); result = 2000;
            em.persist(visa); result = new PersistenceException();
        }};
        ccs.credit(visa, 400);
    }
}
