package org.udg.caes.banking.service;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;
import org.udg.caes.banking.entity.Client;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.Assert.*;

/**
 * Created by deidas on 28/10/16.
 */
public class TestClientService_getClient {
    @Tested
    ClientService cs;

    @Test
    public void GetClientOK(@Injectable final EntityManager em, @Mocked final Client cli) throws Exception{
        new Expectations(){{
            em.get("foo", Client.class); result = cli;
        }};
        Client res = cs.getClient("foo");
        assertSame(cli, res);
    }
}
