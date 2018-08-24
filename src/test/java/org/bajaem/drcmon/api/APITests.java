
package org.bajaem.drcmon.api;

import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.exceptions.DRCProbeException;
import org.bajaem.drcmon.model.PingProbeConfig;
import org.bajaem.drcmon.util.DRCBasicAuthRestWeb;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APITests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    @Test
    public void testCreateConfig()
    {
        final PingProbeConfig conf = new PingProbeConfig();
        initializConfig(conf);

        conf.setHost("127.0.0.1");
        
        final String url = baseURL + "api/probeConfigs";
        LOG.info(url);
        final String user = goodWebKey.getId();
        final String passwd = goodWebKey.getSecret();
        final DRCBasicAuthRestWeb client = new DRCBasicAuthRestWeb(url, user, passwd);
        try
        {
            client.post(PingProbeConfig.class, conf);
        }
        catch (final DRCProbeException e)
        {
            LOG.fatal(e);
            fail(e.getMessage());
        }
    }

}
