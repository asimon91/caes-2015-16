package org.udg.caes.banking.service;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.Client;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by deidas on 16/11/16.
 */
public class TestClientService_getRealBalance {
    @Tested
    ClientService cs;

    Account acc1 = new Account("small", 50);
    Account acc2 = new Account("big", 200);

    @Test
    public void GetRealBalanceOK(@Injectable final EntityManager em, @Mocked final Client cli, @Mocked final CreditCard visa, @Mocked final CreditCard masterCard) throws Exception{
        /*
            I know this test could be shorter, but I prefered to test arrays
            with multiple accounts and credit cards. Shorter test would be the
            same but with only one account and credit card.
         */
        final List<Account> clientAccounts = new ArrayList<Account>();
        final List<CreditCard> client1CreditCards = new ArrayList<CreditCard>();
        final List<CreditCard> client2CreditCards = new ArrayList<CreditCard>();
        client1CreditCards.add(visa);
        client2CreditCards.add(masterCard);

        clientAccounts.add(acc1);
        clientAccounts.add(acc2);
        new Expectations(){{
            em.getClientAccounts(cli); result = clientAccounts;
            em.getCreditCards(acc1); result = client1CreditCards;
            em.getCreditCards(acc2); result = client2CreditCards;
            visa.getCredit(); result = 50;
            masterCard.getCredit(); result = 50;
        }};
        long balance = cs.getRealBalance(cli);
        assertEquals(balance, 150);
    }
}
