package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.model.PortMonProbeConfig;
import org.bajaem.drcmon.probe.PortMonProbe;
import org.bajaem.drcmon.probe.Response;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class PortMonProbeTests extends DBGenerator
{
    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testLocalhost8080()
    {
        final PortMonProbeConfig conf = newPortMonProbeConfig();
        final PortMonProbe p = new PortMonProbe(conf);
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
    public void testLocalhostBadPort()
    {
        final PortMonProbeConfig conf = newPortMonProbeConfig("127.0.0.1", 1);
        final PortMonProbe p = new PortMonProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testLocalhostBadHost()
    {
        final PortMonProbeConfig conf = newPortMonProbeConfig("foo.bar", 1);
        final PortMonProbe p = new PortMonProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNotNull(response.getErrorMessage());
    }
}
