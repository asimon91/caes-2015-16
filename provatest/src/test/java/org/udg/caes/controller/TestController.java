package org.udg.caes.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
  public void testGetHandlerIsNotNull() throws Exception{
    Handler h2 = controller.getHandler(r);
    assertNotNull("Get handler doesn't return null handler", h2);
  }

  @Test
  public void testGetHandlerReturnsSameObject() throws Exception{
    Handler h2 = controller.getHandler(r);
    assertSame(h, h2);
  }

  @Test
  public void testProcessRequest() throws Exception{
    Response res = controller.getHandler(r).process(r);
    assertNotNull("Response is not null", res);
  }

  @Test
  public void testMultipleProcessReturnNotTheSameResponse() throws Exception{
    Response res1 = controller.getHandler(r).process(r);
    SampleRequest r2 = new SampleRequest("test2");
    SampleHandler h2 = new SampleHandler();
    controller.addHandler(r2, h2);
    Response res2 = controller.getHandler(r2).process(r2);
    assertNotSame(res1, res2);
  }

  @Test
  public void testGetTypeOfRequest() throws Exception{
    Request req = new SampleRequest("foo");
    assertEquals("foo", req.getType());
  }

  @Test(expected = Exception.class)
  public void testAddHandlerToExistingRequest() throws Exception{
    SampleHandler h2 = new SampleHandler();
    controller.addHandler(r, h2);
  }

  @Test(expected = Exception.class)
  public void testGetNonExistingHandler() throws Exception{
    SampleRequest foo = new SampleRequest("dummy");
    Handler bar = controller.getHandler(foo);
  }

  private class SampleHandler implements Handler {
    // Is this a mock class? :-)
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
