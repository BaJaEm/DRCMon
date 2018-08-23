
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.exceptions.DRCProbeException;
import org.bajaem.drcmon.util.DRCRestTemplate;
import org.bajaem.drcmon.util.DRCWebClient;
import org.junit.Test;

public class RestWebTests extends DBGenerator
{

    private static final Logger LOG = LogManager.getLogger();

    public RestWebTests() throws FileNotFoundException, SQLException
    {
        LOG.info("new client");
    }

    @Test
    public void testWebClientGET()
    {
        try
        {
            final DRCWebClient client = new DRCRestTemplate("http://127.0.0.1:" + port + "/api/");
            final String foo = client.get(String.class);
            assertNotNull(foo);
            LOG.info(foo);
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
    }
}
