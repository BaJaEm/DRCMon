
package org.bajaem.drcmon.probes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.model.ProbeKey;
import org.bajaem.drcmon.probe.Response;
import org.bajaem.drcmon.probe.WebBotProbe;
import org.bajaem.drcmon.probe.WebBotProbeConfig;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

public class WebBotProbeTests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testGood()
    {
        final WebBotProbeConfig conf = newWebBotProbeConfig();
        final WebBotProbe p = new WebBotProbe(conf);
        final Response response = p.probe();
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNull(response.getError());
        assertNotNull(response.getDataMap());
        assertTrue(response.getDataMap().isEmpty());
        assertNull(response.getErrorMessage());
    }

}
