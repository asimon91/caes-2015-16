package org.udg.caes.controller;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Before;
import org.junit.Test;
import org.udg.caes.controller.exceptions.ProcessErrorException;

import static org.junit.Assert.*;

/**
 * Created by deidas on 18/10/16.
 */
public class TestDefaultController {
    @Tested
    DefaultController cont;
    Handler h1 = new Handler(){
        public Response process(Request r) throws ProcessErrorException {
            return null;
        }
    };
    Response r1 = new Response() {
        public String getContent() {
            return null;
        }
    };

    @Test
    public void testGetHandlerIsNotNull(@Mocked final Request req) throws Exception{
        new Expectations(){{
            req.getType(); returns("MockedRequest1", "MockedRequest1", "MockedRequest1");
        }};
        cont.addHandler(req, h1);
        Handler h2 = cont.getHandler(req);
        assertNotNull("Get handler doesn't return null handler", h2);
    }

    @Test
    public void testGetHandlerReturnsSameObject(@Mocked final Request req) throws Exception{
        new Expectations(){{
           req.getType(); returns("MockedRequest1", "MockedRequest1", "MockedRequest1");
        }};
        cont.addHandler(req, h1);
        Handler h2 = cont.getHandler(req);
        assertSame(h1, h2);
    }

    @Test
    public void testProcessRequest(@Mocked final Request req, @Mocked final Handler han) throws Exception{
        new Expectations(){{
            req.getType(); result = "MockedRequest1";
            han.process(req); result = r1;
        }};
        cont.addHandler(req, han);
        Response res = cont.getHandler(req).process(req);
        assertNotNull("Response is not null", res);
    }

    @Test
    public void testMultipleProcessReturnNotTheSameResponse(
            @Mocked final Request req1, @Mocked final Handler han1,
            @Mocked final Request req2, @Mocked final Handler han2) throws Exception{
        final Response r2 = new Response(){
            public String getContent() {
                return null;
            }
        };
        new Expectations(){{
            req1.getType(); result = "MockedRequest1";
            req2.getType(); result = "MockedRequest2";
            han1.process(req1); result = r1;
            han2.process(req2); result = r2;
        }};

        cont.addHandler(req1, han1);
        cont.addHandler(req2, han2);

        Response res1 = cont.getHandler(req1).process(req1);
        Response res2 = cont.getHandler(req2).process(req2);

        assertNotSame(res1, res2);
    }

    @Test(expected = Exception.class)
    public void testAddHandlerToExistingRequest(@Mocked final Request req) throws Exception{
        new Expectations(){{
            req.getType(); result = "MockedRequest";
        }};
        Handler h2 = new Handler(){
            public Response process(Request r) throws ProcessErrorException {
                return null;
            }
        };
        cont.addHandler(req, h1);
        cont.addHandler(req, h2);
    }

    @Test(expected = Exception.class)
    public void testGetNonExistingHandler(@Mocked final Request req) throws Exception{
        new Expectations(){{
           req.getType(); result = "dummy";
        }};
        Handler bar = cont.getHandler(req);
    }
}
