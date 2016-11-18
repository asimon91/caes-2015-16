package org.udg.caes.banking.service;

import mockit.*;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.omg.CORBA.PERSIST_STORE;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

/**
 * Created by deidas on 18/11/16.
 */
public class TestCreditCardService_invalidate {
    @Tested
    CreditCardService ccs;

    @Injectable
    EntityManager em;

    @Mocked
    CreditCard MasterCard;

    @Test
    public void InvalidateOK() throws Exception {
        ccs.invalidate(MasterCard);
        new Verifications(){{
            MasterCard.setActive(false); times = 1;
            em.persist(MasterCard); times = 1;
        }};
    }

    @Test(expected = PersistenceException.class)
    public void InvalidatePersistenceExceptionError() throws Exception {
        new Expectations(){{
           em.persist(MasterCard); result = new PersistenceException();
        }};
        ccs.invalidate(MasterCard);
    }
}
