package org.udg.caes.banking.service;

import com.google.inject.Inject;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.CreditExceeded;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

/**
 * Created by imartin on 19/10/16.
 */
public class CreditCardService {

  @Inject EntityManager em;

  void credit(CreditCard cc, long amount) throws CreditExceeded, PersistenceException {
    if (cc.getCredit() + amount > cc.getMaxCredit()) throw new CreditExceeded();
    cc.credit(amount);
    em.persist(cc);
  }

  void charge(CreditCard cc, Account acc) throws NotEnoughBalance, PersistenceException {
    if (acc.getBalance() < cc.getCredit()) throw new NotEnoughBalance();
    acc.debit(cc.getCredit());
    cc.reset();
    em.persist(cc);
    em.persist(acc);
  }

  void delete(CreditCard cc) throws NotEnoughBalance, PersistenceException {
    Account acc = em.getAccountAssociated(cc);
    this.charge(cc, acc);
    em.delete(cc);
  }

  void invalidate(CreditCard cc) throws PersistenceException {
    cc.setActive(false);
    em.persist(cc);
  }

}
