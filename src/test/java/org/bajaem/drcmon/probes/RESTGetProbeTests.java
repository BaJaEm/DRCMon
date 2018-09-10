
package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.bajaem.drcmon.model.RESTGetProbeConfig;
import org.bajaem.drcmon.probe.RESTGetProbe;
import org.bajaem.drcmon.probe.Response;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class RESTGetProbeTests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testGood()
    {
        final RESTGetProbeConfig conf = newRESTGetProbeConfig(//
                getURLforProbeType("Ping"), //
                "description", //
                "Ping using java isReachable method - may or maynot be ICMP", //
                goodWebKey);//
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadResult()
    {
        final RESTGetProbeConfig conf = newRESTGetProbeConfig(//
                getURLforProbeType("RESTGet"), //
                "description", //
                "Not Good", //
                goodWebKey);//
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testNoUser()
    {
        final RESTGetProbeConfig conf = newRESTGetProbeConfig(//
                "http://127.0.0.1:" + port + "/api/probeTypes/Ping", //
                "description", //
                "Ping using java isReachable method - may or maynot be ICMP", //
                null);//
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        LOG.info(response.getErrorMessage());
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadHost()
    {
        final RESTGetProbeConfig conf = newRESTGetProbeConfig(//
                "http://foo.bar:" + port + "/api/probeTypes/Ping", //
                "description", //
                "Ping using java isReachable method - may or maynot be ICMP", //
                goodWebKey);//
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        LOG.info(response.getErrorMessage());
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadUserPassword()
    {
        final RESTGetProbeConfig conf = newRESTGetProbeConfig(//
                "http://127.0.0.1:" + port + "/api/probeTypes/Ping", //
                "description", //
                "Ping using java isReachable method - may or maynot be ICMP", //
                badWebKey);//
        final RESTGetProbe p = new RESTGetProbe(conf);
        final Response response = p.probe();
        LOG.info(response.getErrorMessage());
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }
}
