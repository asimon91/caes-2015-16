package org.udg.caes.controller;

import org.udg.caes.controller.exceptions.HandlerAlreadyExistException;
import org.udg.caes.controller.exceptions.HandlerNotFoundException;
import org.udg.caes.controller.exceptions.ProcessErrorException;

/**
 * Created by imartin on 30/09/16.
 */
public interface Controller {
  Response process(Request r) throws HandlerNotFoundException, ProcessErrorException;
  Handler getHandler(Request r) throws HandlerNotFoundException;
  void addHandler(Request r, Handler h) throws HandlerAlreadyExistException;
}
