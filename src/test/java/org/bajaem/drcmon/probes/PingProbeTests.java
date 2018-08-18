package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Calendar;

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
    private static final Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
    private static final String user = "testUser";

    private final PingProbeConfig pingConf = new PingProbeConfig();

    @Before
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void init() throws UnknownHostException
    {
        LOG.trace("Starting test");

        pingConf.setArtifactId(null);
        pingConf.setCreatedBy(user);
        pingConf.setCreatedOn(now);
        pingConf.setDelayTime(0);
        pingConf.setLastModifiedBy(user);
        pingConf.setLastModifiedOn(now);
        pingConf.setPollingInterval(30);

    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testLocalhost()
    {
        pingConf.setHost("127.0.0.1");
        final PingProbe p = new PingProbe(pingConf);
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
        pingConf.setHost("foo.bar");
        final PingProbe p = new PingProbe(pingConf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }
}
