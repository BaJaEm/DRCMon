
package org.bajaem.drcmon;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.util.DRCRestTemplate;
import org.bajaem.drcmon.util.DRCWebClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class WebClientTest
{

    private static final Logger LOG = LogManager.getLogger(WebClientTest.class);

    @LocalServerPort
    private int port;

    public WebClientTest() throws FileNotFoundException, SQLException
    {
        LOG.info("new client");

    }

    @Test
    public void testWebClientGET()
    {
        final DRCWebClient client = new DRCRestTemplate("http://127.0.0.1:" + port + "/api/");
        ;
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
