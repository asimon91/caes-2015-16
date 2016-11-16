package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.Assert.*;

/**
 * Created by deidas on 28/10/16.
 */
public class TestCreditCardService_credit {
    @Tested
    CreditCardService ccs;

    @Test
    public void CreditOK(@Injectable final EntityManager em, @Mocked final CreditCard visa) throws Exception{
        new Expectations(){{
            visa.getCredit(); result = 1500;
            visa.getMaxCredit(); result = 2000;
        }};
        ccs.credit(visa, 500);
        new Verifications(){{
            //ccs.credit(visa, 500); times = 1;
            em.persist(visa); times = 1;
        }};
    }
}
