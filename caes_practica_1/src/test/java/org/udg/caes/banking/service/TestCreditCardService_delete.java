package org.udg.caes.banking.service;

import mockit.*;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

/**
 * Created by deidas on 18/11/16.
 */
public class TestCreditCardService_delete {
    @Tested
    CreditCardService ccs;

    @Injectable
    EntityManager em;

    @Mocked
    Account acc;

    @Mocked
    CreditCard visa;

    @Test
    public void DeleteOK() throws Exception {
        new Expectations(){{
            em.getAccountAssociated(visa); //result = acc, not necessary
        }};
        new Expectations(CreditCardService.class){{
            ccs.charge(visa, acc); times = 1;
        }};
        ccs.delete(visa);
        new Verifications(){{
            em.delete(visa); times = 1;
        }};
    }

    @Test(expected = NotEnoughBalance.class)
    public void DeleteNotEnoughBalanceError() throws Exception {
        new Expectations(){{
            em.getAccountAssociated(visa); //result = acc, not necessary
        }};
        new Expectations(CreditCardService.class){{
           ccs.charge(visa, acc); result = new NotEnoughBalance();
        }};
        ccs.delete(visa);
    }

    @Test(expected = PersistenceException.class)
    public void DeleteChargePersistenceExceptionError() throws Exception {
        new Expectations(){{
            em.getAccountAssociated(visa);
        }};
        new Expectations(CreditCardService.class){{
            ccs.charge(visa, acc); result = new PersistenceException();
        }};
        ccs.delete(visa);
    }

    @Test(expected = PersistenceException.class)
    public void DeletePersistenceExceptionEntityManagerError() throws Exception {
        new Expectations(){{
            em.getAccountAssociated(visa);
            em.delete(visa); result = new PersistenceException();
        }};
        new Expectations(CreditCardService.class){{
            ccs.charge(visa, acc);
        }};
        ccs.delete(visa);
    }
}
