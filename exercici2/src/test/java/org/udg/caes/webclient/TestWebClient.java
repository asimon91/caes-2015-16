package org.udg.caes.webclient;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;

import java.rmi.ConnectIOException;

import static org.junit.Assert.*;

/**
 * Created by deidas on 19/10/16.
 */
public class TestWebClient {
    @Tested WebClient wc;

    @Test
    public void testGetContent(@Mocked final ConnectionFactory cf){
        new Expectations(){{
            cf.getData(); result = "foo";
        }};
        String res = wc.getContent(cf);
        assertEquals("foo", res);
    }

    @Test
    public void testGetContentError(@Mocked final ConnectionFactory cf){
        new Expectations(){{
            cf.getData(); result = new NullPointerException("BOOM");
        }};
        assertNull(wc.getContent(cf));
    }
}
