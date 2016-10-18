package org.udg.caes.controller;

import org.udg.caes.controller.exceptions.ProcessErrorException;

/**
 * Created by imartin on 30/09/16.
 */
public interface Handler {
  Response process(Request r) throws ProcessErrorException;
}
