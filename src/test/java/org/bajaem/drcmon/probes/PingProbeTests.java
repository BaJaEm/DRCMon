package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.model.PingProbeConfig;
import org.bajaem.drcmon.probe.PingProbe;
import org.bajaem.drcmon.probe.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class PingProbeTests extends DBGenerator
{
    private static final Logger LOG = LogManager.getLogger();

    private final PingProbeConfig conf = new PingProbeConfig();

    @Before
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void init() throws UnknownHostException
    {
        LOG.trace("Starting test");

        conf.setArtifactId(null);
        conf.setCreatedBy(user);
        conf.setCreatedOn(now);
        conf.setDelayTime(0);
        conf.setLastModifiedBy(user);
        conf.setLastModifiedOn(now);
        conf.setPollingInterval(30);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testLocalhost()
    {
        conf.setHost("127.0.0.1");
        final PingProbe p = new PingProbe(conf);
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
    public void testBadHost()
    {
        conf.setHost("foo.bar");
        final PingProbe p = new PingProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }
}
