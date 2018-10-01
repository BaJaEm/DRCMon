
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

    private final String test3 = "{\"dataShape\":{\"fieldDefinitions\":{\"name\":{\"name\":\"name\",\"description\":\"Thing name\",\"baseType\":\"STRING\",\"ordinal\":0,\"aspects\":{\"isReadOnly\":true,\"isPersistent\":false,\"isBuiltIn\":true}},\"description\":{\"name\":\"description\",\"description\":\"Thing description\",\"baseType\":\"STRING\",\"ordinal\":0,\"aspects\":{\"isReadOnly\":true,\"isPersistent\":false,\"isBuiltIn\":true}},\"thingTemplate\":{\"name\":\"thingTemplate\",\"description\":\"Thing Template\",\"baseType\":\"THINGTEMPLATENAME\",\"ordinal\":0,\"aspects\":{\"isReadOnly\":true,\"isPersistent\":false,\"isBuiltIn\":true}},\"tags\":{\"name\":\"tags\",\"description\":\"Thing Tags\",\"baseType\":\"TAGS\",\"ordinal\":0,\"aspects\":{\"isReadOnly\":true,\"isPersistent\":false,\"tagType\":\"ModelTags\",\"isBuiltIn\":true}},\"isConnected\":{\"name\":\"isConnected\",\"description\":\"Flag indicating if connected or not\",\"baseType\":\"BOOLEAN\",\"ordinal\":0,\"aspects\":{\"isReadOnly\":true,\"defaultValue\":false,\"isPersistent\":false}},\"lastConnection\":{\"name\":\"lastConnection\",\"description\":\"Last connection time\",\"baseType\":\"DATETIME\",\"ordinal\":0,\"aspects\":{\"isReadOnly\":true,\"defaultValue\":0,\"isPersistent\":true}},\"isActive_ExtensionSourceThingShape_geDiscreteExecution\":{\"name\":\"isActive_ExtensionSourceThingShape_geDiscreteExecution\",\"description\":\"\",\"baseType\":\"BOOLEAN\",\"ordinal\":2,\"aspects\":{\"isReadOnly\":false,\"isPersistent\":true,\"isLogged\":false,\"dataChangeType\":\"VALUE\",\"cacheTime\":0.0}},\"isActive_ExtensionSourceThingShape_geIntegratedDiscreteExecution\":{\"name\":\"isActive_ExtensionSourceThingShape_geIntegratedDiscreteExecution\",\"description\":\"\",\"baseType\":\"BOOLEAN\",\"ordinal\":2,\"aspects\":{\"isReadOnly\":false,\"defaultValue\":true,\"isPersistent\":true,\"isLogged\":false,\"dataChangeType\":\"VALUE\",\"cacheTime\":0.0}}}},\"rows\":[{\"name\":\"GEAExtensionSourceProvider_geIntegratedDiscreteExecution\",\"description\":\"\",\"thingTemplate\":\"RemoteThing\",\"tags\":[{\"vocabulary\":\"geIntegratedDiscreteExecution\",\"vocabularyTerm\":\"All\"}],\"isConnected\":true,\"lastConnection\":1536406381007,\"isActive_ExtensionSourceThingShape_geDiscreteExecution\":true,\"isActive_ExtensionSourceThingShape_geIntegratedDiscreteExecution\":true}]}";

    private final ObjectMapper m = new ObjectMapper();

    private JsonNode node;

    private JsonNode node2;

    private JsonNode node3;

    public JsonToolsTests()
    {
        try
        {
            node = m.readTree(raw);
            node2 = m.readTree(test2);
            node3 = m.readTree(test3);
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
    public void testRows1()
    {
        try
        {
            assertEquals("true", JsonTools.getValue(node3, "rows.0.isConnected"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRows2()
    {
        try
        {
            assertEquals("true", JsonTools.getValue(node3, "rows[0].isConnected"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetValue1()
    {

        try
        {
            assertEquals("a1val", JsonTools.getValue(node, "a.a1"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetValue2()
    {
        try
        {
            assertEquals("b1", JsonTools.getValue(node, "b.0"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetValue3()
    {
        try
        {
            assertEquals("cval", JsonTools.getValue(node, "c"));
        }
        catch (final DRCProbeException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetValue4()
    {
        try
        {
            final String foo = JsonTools.getValue(node, "d");
            assertNotEquals("none", foo);
        }
        catch (final DRCProbeException e)
        {
            // success - this should fail
        }
    }

    @Test
    public void testGetValue5()
    {
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
