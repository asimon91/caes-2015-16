package org.udg.caes.controller;

import org.udg.caes.controller.exceptions.HandlerAlreadyExistException;
import org.udg.caes.controller.exceptions.HandlerNotFoundException;
import org.udg.caes.controller.exceptions.ProcessErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by imartin on 30/09/16.
 */
public class DefaultController implements Controller {
  Map<String, Handler> handlers = new HashMap<String, Handler>();

  public Response process(Request r) throws HandlerNotFoundException, ProcessErrorException{
    try {
      return getHandler(r).process(r);
    } catch (HandlerNotFoundException e) {
      return new ErrorResponse("No handler found for request");
    } catch (ProcessErrorException e) {
      return new ErrorResponse("Error processing request");
    }
  }

  public Handler getHandler(Request r) throws HandlerNotFoundException {
    if (!handlers.containsKey(r.getType())) {
      throw new HandlerNotFoundException();
    }
    return handlers.get(r.getType());
  }

  public void addHandler(Request r, Handler h) throws HandlerAlreadyExistException {
    if (handlers.containsKey(r.getType())) {
      throw new HandlerAlreadyExistException();
    }
    handlers.put(r.getType(), h);
  }
}
