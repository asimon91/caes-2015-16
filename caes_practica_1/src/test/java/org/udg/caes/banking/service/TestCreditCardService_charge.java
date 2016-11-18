package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by deidas on 17/11/16.
 */
public class TestCreditCardService_charge {
    @Tested
    CreditCardService ccs;

    Account acc = new Account("acc", 1000);

    @Test
    public void ChargeOK(@Injectable final EntityManager em, @Mocked final CreditCard visa) throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 400;
        }};
        ccs.charge(visa, acc);
        assertEquals(acc.getBalance(), 600);
        new VerificationsInOrder(){{
            visa.reset(); times = 1;
            em.persist(visa); times = 1;
            em.persist(acc); times = 1;
        }};
    }

    @Test(expected = NotEnoughBalance.class)
    public void ChargeNotEnoughBalance(@Mocked final CreditCard visa) throws Exception {
        new Expectations(){{
            visa.getCredit(); result = 1500;
        }};
        ccs.charge(visa, acc);
    }

//    @Test(expected = NotEnoughBalance.class)
//    public void ChargeNotEnoughBalanceSecond(@Mocked final CreditCard visa) throws Exception {
//        new Expectations(){{
//            visa.getCredit(); returns(500, 1500);
//        }};
//        ccs.charge(visa, acc);
//    }
}
