package org.bajaem.drcmon.probes;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.util.DRCRestTemplate;
import org.bajaem.drcmon.util.DRCWebClient;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;

public class RestWebTests extends DBGenerator
{
    private static final Logger LOG = LogManager.getLogger();

    @LocalServerPort
    private int port;

    public RestWebTests() throws FileNotFoundException, SQLException
    {
        LOG.info("new client");

    }

    @Test
    public void testWebClientGET()
    {
        final DRCWebClient client = new DRCRestTemplate("http://127.0.0.1:" + port + "/api/");
        final String foo = client.get(String.class);
        System.err.print(foo);
    }

    @Test
    public void testWebClientPOSTNoBody()
    {
    }

    @Test
    public void testWebClientPOSTBody()
    {
    }
}
