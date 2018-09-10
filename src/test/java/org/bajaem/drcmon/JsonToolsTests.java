
package org.bajaem.drcmon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.exceptions.DRCProbeException;
import org.bajaem.drcmon.util.JsonTools;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToolsTests
{

    private static final Logger LOG = LogManager.getLogger();

    private final String raw = "{ \"a\" : { \"a1\" : \"a1val\" }, \"b\" : [ \"b1\", \"b2\", \"b3\"], \"c\" : \"cval\" }";

    private final String test2 = "{\r\n" + "  \"artifactId\" : null,\r\n" + "  \"pollingInterval\" : 30,\r\n"
            + "  \"delayTime\" : 0,\r\n" + "  \"createdOn\" : \"2018-08-28T22:22:29.994+0000\",\r\n"
            + "  \"createdBy\" : \"b\",\r\n" + "  \"lastModifiedOn\" : \"2018-09-06T18:01:52.139+0000\",\r\n"
            + "  \"lastModifiedBy\" : \"b\",\r\n" + "  \"enabled\" : true,\r\n" + "  \"customConfiguration\" : {\r\n"
            + "    \"HOST\" : \"VDCAWQ02779.logon.ds.ge.com\"\r\n" + "  },\r\n" + "  \"_links\" : {\r\n"
            + "    \"self\" : {\r\n" + "      \"href\" : \"http://localhost:8080/api/probeConfigs/20392\"\r\n"
            + "    },\r\n" + "    \"probeConfig\" : {\r\n"
            + "      \"href\" : \"http://localhost:8080/api/probeConfigs/20392{?projection}\",\r\n"
            + "      \"templated\" : true\r\n" + "    },\r\n" + "    \"probeKey\" : {\r\n"
            + "      \"href\" : \"http://localhost:8080/api/probeConfigs/20392/probeKey\"\r\n" + "    },\r\n"
            + "    \"probeType\" : {\r\n"
            + "      \"href\" : \"http://localhost:8080/api/probeConfigs/20392/probeType\"\r\n" + "    }\r\n"
            + "  }\r\n" + "}";

    private final ObjectMapper m = new ObjectMapper();

    private JsonNode node;

    private JsonNode node2;

    public JsonToolsTests()
    {
        try
        {
            node = m.readTree(raw);
            node2 = m.readTree(test2);
        }
        catch (final IOException e)
        {
            LOG.error(e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetValueInt()
    {
        try
        {
            assertEquals("30", JsonTools.getValue(node2, "pollingInterval"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }

    }

    @Test
    public void testGetValue()
    {

        try
        {
            assertEquals("a1val", JsonTools.getValue(node, "a.a1"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
        try
        {
            assertEquals("b1", JsonTools.getValue(node, "b.0"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
        try
        {
            assertEquals("cval", JsonTools.getValue(node, "c"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
        try
        {
            final String foo = JsonTools.getValue(node, "d");
            assertNotEquals("none", foo);
        }
        catch (final DRCProbeException e)
        {
            // success - this should fail
        }

        try
        {
            JsonTools.getValue(node, "b.a1");
            fail("should not get here - invalid path");
        }
        catch (final DRCProbeException e)
        {
            // success - this should fail
        }

    }

}
