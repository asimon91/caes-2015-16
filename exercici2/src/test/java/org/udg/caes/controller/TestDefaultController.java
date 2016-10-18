package org.udg.caes.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by deidas on 18/10/16.
 */
public class TestDefaultController {
    private Controller controller;
    private MockedHandler h;
    private MockedRequest r;

//    @Before
//    public void setup() throws Exception {
//        controller = new DefaultController();
//        h = new MockedHandler();
//        r = new MockedRequest("test");
//        controller.addHandler(r, h);
//    }

    @Test
    public void testGetHandlerIsNotNull() throws Exception{
        Handler h2 = controller.getHandler(r);
        assertNotNull("Get handler doesn't return null handler", h2);
    }

//    @Test
//    public void testGetHandlerReturnsSameObject() throws Exception{
//        Handler h2 = controller.getHandler(r);
//        assertSame(h, h2);
//    }
//
//    @Test
//    public void testProcessRequest() throws Exception{
//        Response res = controller.getHandler(r).process(r);
//        assertNotNull("Response is not null", res);
//    }
//
//    @Test
//    public void testMultipleProcessReturnNotTheSameResponse() throws Exception{
//        Response res1 = controller.getHandler(r).process(r);
//        MockedRequest r2 = new MockedRequest("test2");
//        MockedHandler h2 = new MockedHandler();
//        controller.addHandler(r2, h2);
//        Response res2 = controller.getHandler(r2).process(r2);
//        assertNotSame(res1, res2);
//    }
//
//    @Test
//    public void testGetTypeOfRequest() throws Exception{
//        Request req = new MockedRequest("foo");
//        assertEquals("foo", req.getType());
//    }
//
//    @Test(expected = Exception.class)
//    public void testAddHandlerToExistingRequest() throws Exception{
//        MockedHandler h2 = new MockedHandler();
//        controller.addHandler(r, h2);
//    }
//
//    @Test(expected = Exception.class)
//    public void testGetNonExistingHandler() throws Exception{
//        MockedRequest foo = new MockedRequest("dummy");
//        Handler bar = controller.getHandler(foo);
//    }

    private class MockedHandler implements Handler {
        public Response process(Request r) {
            return new MockedResponse();
        }
    }

    private class MockedRequest implements Request {
        String type;

        MockedRequest(String t) { type = t; }

        public String getType() {
            return type;
        }
    }

    private class MockedResponse implements Response {
        public String getContent(){
            return "Aquests son els continguts de la resposta!!! :-)";
        }
    }
}
