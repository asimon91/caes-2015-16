package org.udg.caes.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by imartin on 30/09/16.
 */
public class DefaultController implements Controller {
  Map<String, Handler> handlers = new HashMap<String, Handler>();

  @Override
  public Response process(Request r) {
    try {
      return getHandler(r).process(r);
    } catch (Exception e) {
      return new ErrorResponse();
    }
  }

  @Override
  public Handler getHandler(Request r) throws Exception {
    if (!handlers.containsKey(r.getType())) {
      throw new Exception();
    }
    return handlers.get(r.getType());
  }

  @Override
  public void addHandler(Request r, Handler h) throws Exception {
    if (handlers.containsKey(r.getType())) {
      throw new Exception();
    }
    handlers.put(r.getType(), h);
  }
}
