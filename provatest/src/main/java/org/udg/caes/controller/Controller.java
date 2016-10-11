package org.udg.caes.controller;

/**
 * Created by imartin on 30/09/16.
 */
public interface Controller {
  Response process(Request r);
  Handler getHandler(Request r) throws Exception;
  void addHandler(Request r, Handler h) throws Exception;
}
