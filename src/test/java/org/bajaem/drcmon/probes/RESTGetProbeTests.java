
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
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class RESTGetProbeTests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    private final RESTGetProbeConfig conf = new RESTGetProbeConfig();

    @Before
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void init()
    {
        LOG.trace("Starting test");
        initializConfig(conf);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testGood()
    {
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setKeyFile(goodWebKeyFile.getAbsolutePath());
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
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
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setKeyFile(goodWebKeyFile.getAbsolutePath());
        conf.setExpected("Not Good");
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
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
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
        conf.setUrl("http://foo.bar:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
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
    public void testBadKeyFile()
    {
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setKeyFile("BadFile");
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
        try
        {
            new RESTGetProbe(conf);
            fail("Probe Intialization should fail");
        }
        catch (DRCStartupException e)
        {
            // Correct path
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testBadUserPassword()
    {
        conf.setUrl("http://127.0.0.1:" + port + "/api/probeTypes/Ping");
        conf.setPath("description");
        conf.setKeyFile(badWebKeyFile.getAbsolutePath());
        conf.setExpected("Ping using java isReachable method - may or maynot be ICMP");
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
