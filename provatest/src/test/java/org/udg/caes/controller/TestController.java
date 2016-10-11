package org.udg.caes.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Created by imartin on 30/09/16.
 */
public class TestController {

  private Controller controller;
  private SampleHandler h;
  private SampleRequest r;

  @Before
  public void setup() throws Exception {
    controller = new DefaultController();
    h = new SampleHandler();
    r = new SampleRequest("test");
    controller.addHandler(r, h);
  }

  @Test
  public void testGetOk() throws Exception {
    Handler h2 = controller.getHandler(r);
    assertNotNull("not null", h2);
    assertSame("Same object", h, h2);
  }


  private class SampleHandler implements Handler {

    public Response process(Request r) {
      return new SampleResponse();
    }

  }

  private class SampleRequest implements Request {
    String type;

    SampleRequest(String t) { type = t; }

    public String getType() {
      return type;
    }
  }

  private class SampleResponse implements Response {
  }

}
