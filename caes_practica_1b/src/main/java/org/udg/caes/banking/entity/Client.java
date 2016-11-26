package org.udg.caes.banking.entity;

/**
 * Created by imartin on 19/10/16.
 */
public class Client {
  String id;
  String nif;

  public Client(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
