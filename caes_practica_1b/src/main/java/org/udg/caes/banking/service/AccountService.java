package org.udg.caes.banking.service;

import com.google.inject.Inject;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.*;
import org.udg.caes.banking.manager.EntityManager;

import java.util.List;

/**
 * Created by imartin on 19/10/16.
 */
public class AccountService {

  @Inject EntityManager em;
  @Inject CreditCardService ccService;

  Account getAccount(String id) throws AccountNotFound {
    try {
      return (Account)em.get(id, Account.class);
    } catch (EntityNotFound e) {
      throw new AccountNotFound();
    }
  }

  void transfer(String fromId, String toId, long amount) throws AccountNotFound, PersistenceException, NotEnoughBalance {
    Account from = getAccount(fromId);
    Account to = getAccount(toId);

    from.debit(amount);
    to.credit(amount);

    em.persist(from);
    em.persist(to);
  }

  void delete(String id) throws AccountNotFound, AccountActive, PersistenceException, NotEnoughBalance {
    Account acc = getAccount(id);
    delete(acc);
  }

  void delete(Account acc) throws AccountActive, PersistenceException, NotEnoughBalance {
    if (acc.getBalance() > 0) throw new AccountActive();

    List<CreditCard> ccs = em.getCreditCards(acc);
    for (CreditCard cc : ccs) {
      if (cc.getCredit() > 0)
        throw new AccountActive();
      ccService.delete(cc);
    }

    em.delete(acc);
  }
}
