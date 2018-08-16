
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

    private final ObjectMapper m = new ObjectMapper();

    private JsonNode node;

    public JsonToolsTests()
    {
        try
        {
            node = m.readTree(raw);
        }
        catch (final IOException e)
        {
            LOG.error(e);
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
        catch (DRCProbeException e)
        {
            fail(e.getMessage());
        }
        try
        {
            assertEquals("b1", JsonTools.getValue(node, "b.0"));
        }
        catch (DRCProbeException e)
        {
            fail(e.getMessage());
        }
        try
        {
            assertEquals("cval", JsonTools.getValue(node, "c"));
        }
        catch (DRCProbeException e)
        {
            fail(e.getMessage());
        }
        try
        {
            final String foo = JsonTools.getValue(node, "d");
            assertNotEquals("none", foo);
        }
        catch (DRCProbeException e)
        {
            // success - this should fail
        }

        try
        {
            JsonTools.getValue(node, "b.a1");
            fail("should not get here - invalid path");
        }
        catch (DRCProbeException e)
        {
            // success - this should fail
        }

    }

}
