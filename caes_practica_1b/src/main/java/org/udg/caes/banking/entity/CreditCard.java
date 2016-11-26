package org.udg.caes.banking.entity;

/**
 * Created by imartin on 19/10/16.
 */
public class CreditCard {
  String id;
  String accId;
  long credit;
  long maxCredit;
  private boolean active;

  public CreditCard(String id) {
    this.id = id;
  }

  public long getCredit() {
    return credit;
  }

  public void reset() {
    credit = 0;
  }

  public long getMaxCredit() {
    return maxCredit;
  }

  public void credit(long amount) {
    credit += amount;
  }

  public String getId() {
    return id;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
